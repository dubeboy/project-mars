package com.dubedivine.apps.yerrr.controller.status

import com.dubedivine.apps.yerrr.controller.shared.SharedVoteController
import com.dubedivine.apps.yerrr.model.Status
import com.dubedivine.apps.yerrr.model.StatusVote
import com.dubedivine.apps.yerrr.model.responseEntity.StatusResponseEntity
import com.dubedivine.apps.yerrr.repository.status.StatusRepository
import com.dubedivine.apps.yerrr.repository.status.StatusVoteRepository
import com.dubedivine.apps.yerrr.repository.UserRepository
import com.dubedivine.apps.yerrr.repository.status.findByIdOrNull
import com.dubedivine.apps.yerrr.utils.KUtils
import com.dubedivine.apps.yerrr.utils.KUtils.PAGE_SIZE
import com.dubedivine.apps.yerrr.utils.Response
import com.dubedivine.apps.yerrr.utils.createdResponse
import com.dubedivine.apps.yerrr.utils.response
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.gridfs.GridFsOperations
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("statuses")
class StatusesController(private val userRepository: UserRepository,
                         private val repository: StatusRepository,
                         private val voteRepository: StatusVoteRepository,
                         private val gridFSOperations: GridFsOperations) {

    @GetMapping
    fun all(@RequestParam page: Int? = 0): Response<List<Status>> {
        // TODO: Order by geolocation and filter by not deleted
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
        val insertedStatus = repository.insert(status)
        return createdResponse(STATUS_POSTED, insertedStatus)
    }

    /**
     * does not return the comments of the status
    * */
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

    // TODO: could actually move this to its own controller

    @PostMapping("vote")
    fun vote(@RequestBody vote: StatusVote): ResponseEntity<StatusResponseEntity<Boolean>> {
        return SharedVoteController.vote(voteRepository, repository, vote, userRepository)
    }

    @PostMapping("vote/delete")
    fun removeVote(@RequestBody vote: StatusVote): ResponseEntity<StatusResponseEntity<Boolean>> {
        return SharedVoteController.removeVote(voteRepository, repository, vote, userRepository)
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
