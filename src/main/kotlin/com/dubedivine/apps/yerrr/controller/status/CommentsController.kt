package com.dubedivine.apps.yerrr.controller.status

import com.dubedivine.apps.yerrr.controller.shared.SharedVoteController
import com.dubedivine.apps.yerrr.model.Comment
import com.dubedivine.apps.yerrr.model.Status
import com.dubedivine.apps.yerrr.model.CommentVote
import com.dubedivine.apps.yerrr.model.responseEntity.StatusResponseEntity
import com.dubedivine.apps.yerrr.repository.StatusCommentVoteRepository
import com.dubedivine.apps.yerrr.repository.StatusRepository
import com.dubedivine.apps.yerrr.repository.UserRepository
import com.dubedivine.apps.yerrr.repository.findByIdOrNull
import com.dubedivine.apps.yerrr.utils.response
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("statuses/{status_id}/comments")
class CommentsController(private val userRepository: UserRepository,
                         private val repository: StatusRepository,
                         private val voteRepository: StatusCommentVoteRepository,
                         private val mongoTemplate: MongoTemplate) {

    @GetMapping
    fun getComments(@PathVariable("status_id") statusId: String, @RequestParam("page") page: Int? = 0): ResponseEntity<StatusResponseEntity<List<Comment>>> { // TODO: boxing and unboxing of values fro primitves
        val status = repository.findByIdAndPageComments(statusId, page ?: 0) ?: return response(StatusesController.STATUS_NOT_FOUND, null, false)
        return response("", status.comments) // TODO: should use criteria to page response
    }

    @PostMapping
    fun comment(@RequestBody comment: Comment, @PathVariable("status_id") statusId: String): ResponseEntity<StatusResponseEntity<String>> {
        val status = repository.findByIdOrNull(statusId) ?: return response(StatusesController.STATUS_NOT_FOUND, null, false)  // TODO: SLICE Children to 0,0 so that there are no children!
        status.comments.add(comment)
        repository.save(status)
        return response("", comment.id)
    }

    @PostMapping("vote")
    fun voteOnComment(@RequestBody vote: CommentVote,
                      @PathVariable("status_id") statusId: String): ResponseEntity<StatusResponseEntity<Boolean>> {
        val status = getComment(vote.id.entityId, statusId)
        val comment = status?.comments?.firstOrNull() // TODO: dhoes this update or replace
        when {
            comment != null -> {
                val entity = SharedVoteController.voteOnEntity(voteRepository, comment, vote)
                return when {
                    entity != null -> {
                        status.comments.add(entity)
                        repository.save(status)
                        response(StatusesController.VOTE_TWICE_SHOULD_NOT_HAPPEN, true)
                    }
                    else -> {
                        // Vote exits
                        response(StatusesController.COMMENT_NOT_FOUND , false)
                    }
                }
            }
            else -> {
                return response(StatusesController.COMMENT_NOT_FOUND , null)
            }
        }
    }

    @PostMapping("vote/delete")
    fun removeVoteOnComment(@RequestBody vote: CommentVote,
                            @PathVariable("status_id") statusId: String): ResponseEntity<StatusResponseEntity<Boolean>> {
        val status = getComment(vote.id.entityId, statusId)
        val comment = status?.comments?.firstOrNull()
        when {
            comment != null -> {
                val entity = SharedVoteController.removeVoteOnEntity(voteRepository, comment, vote)
                return when {
                    entity != null -> {
                        status.comments.add(entity)
                        repository.save(status)
                        response(StatusesController.VOTE_TWICE_SHOULD_NOT_HAPPEN, true)
                    }
                    else -> {
                        // Vote exits
                        response(StatusesController.COMMENT_NOT_FOUND , false)
                    }
                }
            }
            else -> {
                return response(StatusesController.COMMENT_NOT_FOUND , null)
            }
        }
    }

    private fun getComment(commentId: String, statusId: String): Status? {
        val query = Query()
        query.addCriteria(where("_id").isEqualTo(ObjectId(statusId)).and("comments._id").isEqualTo(ObjectId(commentId)))
        query.fields().position("comments", 1)
        val status: List<Status>? = mongoTemplate.find(query, Status::class.java)

        return status?.firstOrNull()
    }

    companion object {
        const val COMMENT_VOTED = "Voted on comment."
    }
}