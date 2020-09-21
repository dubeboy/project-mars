package com.dubedivine.apps.yerrr.controller.status

import com.dubedivine.apps.yerrr.controller.shared.SharedVoteController
import com.dubedivine.apps.yerrr.controller.users.UsersController
import com.dubedivine.apps.yerrr.model.Status
import com.dubedivine.apps.yerrr.model.requestObject.StatusLike
import com.dubedivine.apps.yerrr.model.requestObject.StatusVote
import com.dubedivine.apps.yerrr.model.responseEntity.StatusResponseEntity
import com.dubedivine.apps.yerrr.repository.UserRepository
import com.dubedivine.apps.yerrr.repository.status.StatusLikeRepository
import com.dubedivine.apps.yerrr.repository.status.StatusRepository
import com.dubedivine.apps.yerrr.repository.status.StatusVoteRepository
import com.dubedivine.apps.yerrr.repository.status.findByIdOrNull
import com.dubedivine.apps.yerrr.utils.KUtils
import com.dubedivine.apps.yerrr.utils.KUtils.PAGE_SIZE
import com.dubedivine.apps.yerrr.utils.Response
import com.dubedivine.apps.yerrr.utils.createdResponse
import com.dubedivine.apps.yerrr.utils.response
import org.bson.types.ObjectId
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.gridfs.GridFsOperations
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("statuses")
class StatusesController(private val userRepository: UserRepository,
                         private val repository: StatusRepository,
                         private val voteRepository: StatusVoteRepository,
                         private val likeRepository: StatusLikeRepository,
                         private val mongoTemplate: MongoTemplate,
                         private val gridFSOperations: GridFsOperations) {

    @GetMapping
    fun all(@RequestParam page: Int? = 0): Response<List<Status>> {
        // TODO: Order by geolocation and filter by not deleted
        //for page zero we do not increase the coordinates and we increase in subsequent pages by a certain rage
        // https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/#mongo.geospatial
//        val circle = Circle(-73.99171, 40.738868, 0.003712240453784)
//        val venues: List<Venue> = template.find(Query(Criteria.where("location").withinSphere(circle)), Venue::class.java)
        val pageable = PageRequest.of(page ?: 0, PAGE_SIZE)
        val statuses = repository.findAll(pageable).content
        return response("", statuses)
    }

    // TODO: maybe create a handshake JWT everytime a user is posting to verify device, we could use FCM
    // also limit the number of characters
    @PutMapping
    fun create(@RequestBody status: Status): Response<Status> {
//        val user = userRepository.findByIdOrNull(status.user.id)
//                ?: return badRequestResponse("Something went wrong, please try logging in again", null)
//        status.user = user
        // prevent usets from posting the same status
        status.sanitizeStatus()
        val insertedStatus = repository.insert(status)
        return createdResponse(STATUS_POSTED, insertedStatus)
    }

    /**
     * does not return the comments of the status
     * */
    @GetMapping("{status_id}")
    fun getStatus(@PathVariable("status_id") statusId: String): Response<Status> {
        val status = repository.findByIdOrNull(statusId)
        return when (status != null) {
            true -> {
                response("", status)
            }
            false -> {
                response(STATUS_NOT_FOUND, null, false)
            }
        }
    }

    @PostMapping("vote")
    fun vote(@RequestBody vote: StatusVote): ResponseEntity<StatusResponseEntity<Boolean>> {
        return SharedVoteController.vote(voteRepository, repository, vote, userRepository)
    }

    @PostMapping("vote/delete")
    fun removeVote(@RequestBody vote: StatusVote): ResponseEntity<StatusResponseEntity<Boolean>> {
        return SharedVoteController.removeVote(voteRepository, repository, vote, userRepository)
    }

    @PostMapping("like")
    fun like(@RequestBody statusLike: StatusLike): ResponseEntity<StatusResponseEntity<Boolean>> {
        val status = repository.findByIdOrNull(statusLike.id.entityId)
        val user = userRepository.findByIdOrNull(statusLike.id.userId)
        return when (user == null || status == null) {
            true -> {
                // TODO: log errors like this here
                response("$STATUS_NOT_FOUND or ${UsersController.USER_NOT_FOUND}", null, false)
            }
            else -> {
                statusLike.valueWhenVoted += 1
                return if (statusLike.valueWhenVoted > 50) {
                    response(LIKE_STATUS_CANNOT_BE_MORE_THAN_FIFTY, false)
                } else {
                    statusLike.sanitize()
                    likeRepository.save(statusLike)
                    mongoTemplate.findAndModify(
                            Query.query(Criteria.where("_id").isEqualTo(ObjectId(statusLike.id.entityId))),
                            Update().inc("likes", 1),
                            Status::class.java
                    )
                    response(LIKE_STATUS, true)
                }
            }
        }
    }

    private fun createHandle(handle: String): String {
        return "@$handle"
    }

    @PostMapping
    fun update(@RequestBody status: Status): Response<Status> {
        val savedStatus = repository.save(status)
        return response("Succesfully updated status", savedStatus)
    }

    @PostMapping("{status_id}/files")
    fun addFiles(@PathVariable("status_id") statusId: String,
                 @RequestPart files: List<MultipartFile>): ResponseEntity<StatusResponseEntity<Status>> {
        return when (val status = repository.findByIdOrNull(statusId)) {
            null -> {
                response(FILE_CANNOT_FIND_STATUS, null, false, HttpStatus.NOT_FOUND)
            }
            else -> {
                val media = KUtils.createMedia(files, gridFSOperations)
                // status.media.addAll(media)
                status.media = media
                repository.save(status)
                response(MEDIA_SAVED, repository.save(status))
            }
        }
    }

    companion object {
        const val STATUS_POSTED = "Posted new status"
        const val FILE_CANNOT_FIND_STATUS = "Sorry could not add files because we could not find that Status."
        const val MEDIA_SAVED = "Posted media."
        const val STATUS_NOT_FOUND = "Sorry, could not find that Status."
        const val COMMENT_NOT_FOUND = "Sorry could not find this comment"
        const val VOTE_TWICE_SHOULD_NOT_HAPPEN = "Vote twice should not happen"
        const val LIKE_STATUS = "+1"
        const val LIKE_STATUS_CANNOT_BE_MORE_THAN_FIFTY = "You cannot like the same status more than 50 times."
        const val VOTE_YOU_HAVE_NOT_VOTED_YET = "You have not voted on this Status therefore we cannot remove it"
        const val VOTE_REMOVED = "Vote removed"
        const val VOTED = "%s voted."
    }
}
