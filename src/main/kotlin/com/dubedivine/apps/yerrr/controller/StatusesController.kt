package com.dubedivine.apps.yerrr.controller

import com.dubedivine.apps.yerrr.repository.StatusRepository
import com.dubedivine.apps.yerrr.repository.UserRepository
import com.dubedivine.apps.yerrr.repository.VoteRepository
import com.dubedivine.apps.yerrr.model.Status
import com.dubedivine.apps.yerrr.model.Vote
import com.dubedivine.apps.yerrr.model.responseEntity.StatusResponseEntity
import com.dubedivine.apps.yerrr.utils.KUtils
import com.dubedivine.apps.yerrr.utils.Response
import com.dubedivine.apps.yerrr.utils.createdResponse
import com.dubedivine.apps.yerrr.utils.response
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
                         private val voteRepository: VoteRepository,
                         private val gridFSOperations: GridFsOperations) {

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

    @PostMapping("vote/delete")
    fun removeVote(@RequestBody vote: Vote): ResponseEntity<StatusResponseEntity<Boolean>> {
        val status = repository.findByIdOrNull(vote.id.statusId)
                ?: return response(STATUS_NOT_FOUND, null, false, HttpStatus.NOT_FOUND)
        val existingVote = voteRepository.findById(vote.id).orElse(null)

        return when {
            existingVote != null -> { // we just undo the last action
                existingVote.isDeleted = true // remove the vote
                voteRepository.save(existingVote)
                if (existingVote.valueWhenVoted != status.votes) updateStatusVotes(!existingVote.direction, status)
                else response(VOTE_REMOVED, true)
            }
            else -> {
                response(VOTE_YOU_HAVE_NOT_VOTED_YET, null, false, HttpStatus.BAD_REQUEST)
            }
        }
    }


    @PostMapping("vote")
    fun vote(@RequestBody vote: Vote): ResponseEntity<StatusResponseEntity<Boolean>> {
        val status = repository.findByIdOrNull(vote.id.statusId)
                ?: return response(STATUS_NOT_FOUND, null, false, HttpStatus.NOT_FOUND)
        val existingVote = voteRepository.findById(vote.id).orElse(null)
        when {
            existingVote != null -> {
                return when (existingVote.direction == vote.direction) {
                    true -> {
                        response(VOTE_TWICE_SHOULD_NOT_HAPPEN, null, false, HttpStatus.FORBIDDEN)
                    }
                    else -> { // UPDATE!! Direction is not the same!
                        existingVote.direction = vote.direction
                        voteRepository.save(existingVote)
                        updateStatusVotes(vote.direction, status)
                    }
                }
            } // New Vote
            else -> {
                vote.valueWhenVoted = status.votes
                voteRepository.save(vote)
                return updateStatusVotes(vote.direction, status)
            }
        }
    }

    private fun updateStatusVotes(direction: Boolean, status: Status): ResponseEntity<StatusResponseEntity<Boolean>> {
        if (direction) status.votes += 1 else status.votes -= 1
        repository.save(status)
        return response(VOTED.format(if (direction) "up" else "down"), true)
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
        const val VOTE_TWICE_SHOULD_NOT_HAPPEN = "Vote twice should not happen"
        const val VOTE_YOU_HAVE_NOT_VOTED_YET = "You have not voted on this Status therefore we cannot remove it"
        const val VOTE_REMOVED = "Vote removed"
        const val VOTED = "%s voted."
    }

}