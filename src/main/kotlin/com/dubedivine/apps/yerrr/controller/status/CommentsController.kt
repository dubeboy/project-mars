package com.dubedivine.apps.yerrr.controller.status

import com.dubedivine.apps.yerrr.controller.shared.SharedVoteController
import com.dubedivine.apps.yerrr.model.Comment
import com.dubedivine.apps.yerrr.model.Status
import com.dubedivine.apps.yerrr.model.CommentVote
import com.dubedivine.apps.yerrr.model.responseEntity.StatusResponseEntity
import com.dubedivine.apps.yerrr.repository.status.StatusCommentVoteRepository
import com.dubedivine.apps.yerrr.repository.status.StatusRepository
import com.dubedivine.apps.yerrr.repository.UserRepository
import com.dubedivine.apps.yerrr.utils.response
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
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
    fun getComments(@PathVariable("status_id") statusId: String, @RequestParam("page") page: Int? = 0): ResponseEntity<StatusResponseEntity<List<Comment>>> { // TODO: boxing and unboxing of values fro primitives
        val status = repository.findByIdAndPageComments(statusId, page ?: 0) ?: return response(StatusesController.STATUS_NOT_FOUND, null, false)
        return response("", status.comments) // TODO: should use criteria to page response
    }

    @PostMapping
    fun comment(@RequestBody comment: Comment, @PathVariable("status_id") statusId: String): ResponseEntity<StatusResponseEntity<String>> {
       // TODO: Add the sorting to order the comments to our liking
        val writeResult = mongoTemplate.updateFirst(
                Query(
                        where("_id").isEqualTo(ObjectId(statusId))
                ),
                Update().push("comments", comment), Status::class.java
        )
        return if (writeResult.wasAcknowledged())
            response("", comment.id)
        else
            response(StatusesController.STATUS_NOT_FOUND, null, false)

    }

    @PostMapping("vote")
    fun voteOnComment(@RequestBody vote: CommentVote,
                      @PathVariable("status_id") statusId: String): ResponseEntity<StatusResponseEntity<Boolean>> {
        val status = getComment(vote.id.entityId, statusId)

        val comment = status?.comments?.firstOrNull() // TODO: dhoes this update or replace, IT does not adhere to it, will need to use mongo repository
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