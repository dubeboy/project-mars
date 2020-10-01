package com.dubedivine.apps.yerrr.repository.circle

import com.dubedivine.apps.yerrr.model.PeopleCircle
import org.springframework.data.mongodb.repository.MongoRepository

interface CirclesRepository: MongoRepository<PeopleCircle, String>