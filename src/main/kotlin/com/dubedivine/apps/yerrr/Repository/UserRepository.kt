package com.dubedivine.apps.yerrr.Repository

import com.dubedivine.apps.yerrr.model.Status
import com.dubedivine.apps.yerrr.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, String>