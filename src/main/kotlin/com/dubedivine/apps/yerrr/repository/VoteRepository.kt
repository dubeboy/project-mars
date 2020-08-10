package com.dubedivine.apps.yerrr.repository

import com.dubedivine.apps.yerrr.model.UserStatusID
import com.dubedivine.apps.yerrr.model.Vote
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.*

interface VoteRepository: MongoRepository<Vote, UserStatusID> {

    /**
     * Find by ID and is not deleted
    * */

    @Query("{'_id': ?0, 'isDeleted' : false}")
    override fun findById(id: UserStatusID): Optional<Vote>
}