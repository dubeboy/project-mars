package com.dubedivine.apps.yerrr.repository.shared

import com.dubedivine.apps.yerrr.model.UserEntityID
import com.dubedivine.apps.yerrr.model.abstractEntity.Vote
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import java.util.*

interface VoteRepository<T: Vote>: MongoRepository<T, UserEntityID> {

    /**
     * Find by ID and is not deleted
    * */

    @Query("{'_id': ?0, 'isDeleted' : false}")
    override fun findById(id: UserEntityID): Optional<T>
}