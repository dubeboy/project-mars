package com.dubedivine.apps.yerrr.controller

import com.dubedivine.apps.yerrr.Repository.StatusRepository
import com.dubedivine.apps.yerrr.Repository.UserRepository
import com.dubedivine.apps.yerrr.model.Status
import com.dubedivine.apps.yerrr.model.User
import com.dubedivine.apps.yerrr.model.responseEntity.StatusResponseEntity
import com.dubedivine.apps.yerrr.utils.badRequestResponse
import com.dubedivine.apps.yerrr.utils.createdResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("statuses")
class StatusesController(private val userRepository: UserRepository,
                         private val repository: StatusRepository) {

    fun all() {

    }

    @PutMapping
    fun create(@RequestBody status: Status): ResponseEntity<StatusResponseEntity<Status>> {
        val user = userRepository.findByIdOrNull(status.user.id)
                ?: return badRequestResponse("Something went wrong, please try logging in again", null)
        status.user = user
        val insertedStatus = repository.insert(status)
        return createdResponse("Successfully posted new status", insertedStatus)
    }

    fun update() {

    }
}