package com.dubedivine.apps.yerrr.controller.shared

import com.dubedivine.apps.yerrr.controller.status.StatusesController
import com.dubedivine.apps.yerrr.model.UserEntityID
import com.dubedivine.apps.yerrr.model.StatusVote
import com.dubedivine.apps.yerrr.model.abstractEntity.Votable
import com.dubedivine.apps.yerrr.model.responseEntity.StatusResponseEntity
import com.dubedivine.apps.yerrr.utils.response
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


/**
* Shared voting!!!
 * Could make this an injectable class ??? for now its just a singleton
* */

object SharedVoteController {

    fun <T: Votable, V: MongoRepository<StatusVote, UserEntityID>> removeVote(voteRepository: V,
                                                                              repository: MongoRepository<T, String>,
                                                                              vote: StatusVote): ResponseEntity<StatusResponseEntity<Boolean>> {
        val votableEntity = repository.findByIdOrNull(vote.id.entityId)
                ?: return response(StatusesController.STATUS_NOT_FOUND, null, false, HttpStatus.NOT_FOUND)
        val existingVote = voteRepository.findById(vote.id).orElse(null)

        return when {
            existingVote != null -> { // we just undo the last action
                existingVote.isDeleted = true // remove the vote
                voteRepository.save(existingVote)
                if (existingVote.valueWhenVoted != votableEntity.votes) updateStatusVotes(!existingVote.direction, votableEntity, repository)
                else response(StatusesController.VOTE_REMOVED, true)
            }
            else -> {
                response(StatusesController.VOTE_YOU_HAVE_NOT_VOTED_YET, null, false, HttpStatus.BAD_REQUEST)
            }
        }
    }

    fun <T : Votable, V : MongoRepository<StatusVote, UserEntityID>> removeVoteOnEntity(voteRepository: V,
                                                                                        votableEntity: T,
                                                                                        vote: StatusVote): T? {
        val existingVote = voteRepository.findById(vote.id).orElse(null)

        return when {
            existingVote != null -> { // we just undo the last action
                existingVote.isDeleted = true // remove the vote
                voteRepository.save(existingVote)
                return if (existingVote.valueWhenVoted != votableEntity.votes) {
                    if (vote.direction) votableEntity.votes += 1 else votableEntity.votes -= 1
                    votableEntity
                } else {
                    null
                }
            }
            else -> {
                null
            }
        }
    }

    /**
     * Saves the status Vote
     * and returns you the entity that was voted on with the correct number of votes
     * return null if the voting failed meaning that the user has already voted
    * */
    fun <T : Votable, V : MongoRepository<StatusVote, UserEntityID>> voteOnEntity(voteRepository: V,
                                                                                  entity: T,
                                                                                  vote: StatusVote): T? {
        val existingVote = voteRepository.findById(vote.id).orElse(null)
        when {
            existingVote != null -> {
                return when (existingVote.direction == vote.direction) {
                    true -> {
                       null
                    }
                    else -> { // UPDATE!! Direction is not the same!
                        existingVote.direction = vote.direction
                        voteRepository.save(existingVote)
                        if (vote.direction)  entity.votes += 1 else entity.votes -= 1
                        return entity
                    }
                }
            } // New Vote
            else -> {
                vote.valueWhenVoted = entity.votes
                voteRepository.save(vote)
                if (vote.direction)  entity.votes += 1 else entity.votes -= 1
                return entity
            }
        }
    }

    // TODO: Use vote on entity internally
    fun <T: Votable, V: MongoRepository<StatusVote, UserEntityID>> vote(voteRepository: V,
                                                                        repository: MongoRepository<T, String>,
                                                                        vote: StatusVote): ResponseEntity<StatusResponseEntity<Boolean>> {
        val entity = repository.findByIdOrNull(vote.id.entityId)
                ?: return response(StatusesController.STATUS_NOT_FOUND, null, false, HttpStatus.NOT_FOUND)
        val existingVote = voteRepository.findById(vote.id).orElse(null)
        when {
            existingVote != null -> {
                return when (existingVote.direction == vote.direction) {
                    true -> {
                        response(StatusesController.VOTE_TWICE_SHOULD_NOT_HAPPEN, null, false, HttpStatus.FORBIDDEN)
                    }
                    else -> { // UPDATE!! Direction is not the same!
                        existingVote.direction = vote.direction
                        voteRepository.save(existingVote)
                        updateStatusVotes(vote.direction, entity, repository)
                    }
                }
            } // New Vote
            else -> {
                vote.valueWhenVoted = entity.votes
                voteRepository.save(vote)
                return updateStatusVotes(vote.direction, entity, repository)
            }
        }
    }

    private fun <T: Votable> updateStatusVotes(direction: Boolean, status: T, repository: MongoRepository<T, String>): ResponseEntity<StatusResponseEntity<Boolean>> {
        if (direction) status.votes += 1 else status.votes -= 1
        repository.save(status)
        return response(StatusesController.VOTED.format(if (direction) "up" else "down"), true)
    }
}