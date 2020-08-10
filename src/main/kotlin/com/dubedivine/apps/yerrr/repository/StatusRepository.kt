package com.dubedivine.apps.yerrr.repository

import com.dubedivine.apps.yerrr.model.Comment
import com.dubedivine.apps.yerrr.model.Status
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface StatusRepository: MongoRepository<Status, String> {

    @Query("{'comments._id': ?1, '_id': ?0 }, {'comments.\$': 1}")
    fun findByStatusIdAndCommentId(statusId: String,  commentId: String): Status? // SO that we can like a comment
}