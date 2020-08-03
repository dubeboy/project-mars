package com.dubedivine.apps.yerrr.Repository

import com.dubedivine.apps.yerrr.model.Status
import org.springframework.data.mongodb.repository.MongoRepository

interface StatusRepository: MongoRepository<Status, String>