package com.dubedivine.apps.yerrr.repository.circle

import com.dubedivine.apps.yerrr.model.PeopleCircle
import com.dubedivine.apps.yerrr.model.Status
import com.dubedivine.apps.yerrr.utils.KUtils
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query

interface CirclesRepository: MongoRepository<PeopleCircle, String> {
    @Query("{'name': ?0  }", fields = "{'statuses': { \$slice: [?1, ${KUtils.PAGE_SIZE}] }}")
    fun getStatusesForCircle(name: String, offset: Int): PeopleCircle?
}