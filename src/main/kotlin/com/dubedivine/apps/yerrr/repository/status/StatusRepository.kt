package com.dubedivine.apps.yerrr.repository.status

import com.dubedivine.apps.yerrr.model.Status
import com.dubedivine.apps.yerrr.utils.KUtils.PAGE_SIZE
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.*

interface StatusRepository: MongoRepository<Status, String> {

    @Query("{'comments._id': ?1, '_id': ?0 }, {'comments.\$': 1}")
    fun findByStatusIdAndCommentId(statusId: String,  commentId: String): Status? // SO that we can like a comment

    @Query("{'user._id': ?0 }")
    fun findByUserId(userId: String): List<Status>?

    @Query("{'_id': ?0 }", fields = "{'comments': { \$slice: [?1, $PAGE_SIZE] }}" )
    fun findByIdAndPageComments(id: String, offset: Int): Status?

    /**
     * Eliminate all the comments for a fater query we hope!!!
    * */
    // WORK around to eliminate comments from find by id
    @Query("{ '_id': ?0 }", fields = "{'comments': { \$slice: 0 }}" )
    override fun findById(id: String): Optional<Status>

    @Query("{ }", fields = "{'comments': { \$slice: 0 }}" )
    override fun findAll(pageable: Pageable): Page<Status>
}
// TODO: implement this when the app blows up

fun StatusRepository.findByIdOrNull(id: String): Status? = findById(id).orElse(null)