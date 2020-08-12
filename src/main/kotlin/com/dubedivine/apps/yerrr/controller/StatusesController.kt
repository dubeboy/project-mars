package com.dubedivine.apps.yerrr.controller

import com.dubedivine.apps.yerrr.model.Comment
import com.dubedivine.apps.yerrr.model.Status
import com.dubedivine.apps.yerrr.model.responseEntity.StatusResponseEntity
import com.dubedivine.apps.yerrr.repository.StatusRepository
import com.dubedivine.apps.yerrr.repository.UserRepository
import com.dubedivine.apps.yerrr.repository.shared.VoteRepository
import com.dubedivine.apps.yerrr.utils.KUtils
import com.dubedivine.apps.yerrr.utils.Response
import com.dubedivine.apps.yerrr.utils.createdResponse
import com.dubedivine.apps.yerrr.utils.response
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
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
//                         private val voteRepository: VoteRepository,
                         private val gridFSOperations: GridFsOperations,
                         private val mongoTemplate: MongoTemplate) {

    @GetMapping
    fun all(): Response<List<Status>> {
        // TODO: Order by geolocation and filter by not deleted
        return response("", repository.findAll())
    }

    // TODO: maybe create a handshake JWT everytime a user is posting to verify device, we could use FCM
    // also limit the number of characters
    @PutMapping
    fun create(@RequestBody status: Status): Response<Status> {
//        val user = userRepository.findByIdOrNull(status.user.id)
//                ?: return badRequestResponse("Something went wrong, please try logging in again", null)
//        status.user = user
        // prevent usets from posting the same status
        val insertedStatus = repository.insert(status)
        return createdResponse(STATUS_POSTED, insertedStatus)
    }

    // should return a Pair rather
    @GetMapping("{status_id}")
    fun getStatus(@PathVariable("status_id") statusId: String): ResponseEntity<StatusResponseEntity<Status>> {
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

    // TODO:  SHOULD GO TO ONE CLASS: COMMENT

    @PostMapping("{status_id}/comment")
    fun comment(@RequestBody comment: Comment, @PathVariable("status_id") statusId: String): ResponseEntity<StatusResponseEntity<String>> {
        val status = repository.findByIdOrNull(statusId) ?: return response(STATUS_NOT_FOUND, null, false)
        status.comments.add(comment)
        repository.save(status)
        return response("", comment.id)
    }

    @GetMapping("{status_id}/comment/{comment_id}")
    fun getComment(@PathVariable("comment_id") commentId: String,
                   @PathVariable("status_id") statusId: String): ResponseEntity<StatusResponseEntity<Comment>> {
        val query = Query()
        query.addCriteria(Criteria.where("_id").`is`(ObjectId(statusId)).and("comments._id").isEqualTo(ObjectId(commentId)))
        query.fields().position("comments", 1)
        val status: List<Status>? = mongoTemplate.find(query, Status::class.java)
        return response("", status?.first()?.comments?.first())
    }

    @GetMapping("{status_id}/comment")
    fun getComments(@PathVariable("status_id") statusId: String): ResponseEntity<StatusResponseEntity<List<Comment>>> {
        val status = repository.findByIdOrNull(statusId) ?: return response(STATUS_NOT_FOUND, null, false)
        return response("", status.comments) // TODO: should use criteria to page response
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
        const val VOTE_YOU_HAVE_NOT_VOTED_YET = "You have not voted on this Status therefore we cannot remove it"
        const val VOTE_REMOVED = "Vote removed"
        const val VOTED = "%s voted."
    }

}
