package com.dubedivine.apps.yerrr.repository.status

import com.dubedivine.apps.yerrr.model.requestObject.StatusLike
import com.dubedivine.apps.yerrr.repository.shared.VoteRepository
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.stereotype.Repository

interface StatusLikeRepository: VoteRepository<StatusLike>

@Repository
class StatusLikeRepositoryImplementation(private val repository: StatusLikeRepository,
                                         private val template: MongoTemplate ) {

    fun inc(statusLike: StatusLike): StatusLike? {
        return template.findAndModify(
                Query.query(where("_id").isEqualTo(statusLike.id)),
                Update().inc("valueWhenVoted", 1),
                StatusLike::class.java
        )
    }

    fun findById(statusLike: StatusLike) {

    }
}