package com.dubedivine.apps.yerrr.controller

import com.dubedivine.apps.yerrr.Repository.StatusRepository
import com.dubedivine.apps.yerrr.Repository.UserRepository
import com.dubedivine.apps.yerrr.model.Status
import com.dubedivine.apps.yerrr.utils.Response
import com.dubedivine.apps.yerrr.utils.badRequestResponse
import com.dubedivine.apps.yerrr.utils.createdResponse
import com.dubedivine.apps.yerrr.utils.response
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("statuses")
class StatusesController(private val userRepository: UserRepository,
                         private val repository: StatusRepository) {

    @GetMapping
    fun all(): Response<List<Status>> {
        // TODO: Order by geolocation
        return response("", repository.findAll())
    }

    @PutMapping
    fun create(@RequestBody status: Status): Response<Status> {
        val user = userRepository.findByIdOrNull(status.user.id)
                ?: return badRequestResponse("Something went wrong, please try logging in again", null)
        status.user = user
        val insertedStatus = repository.insert(status)
        return createdResponse("Successfully posted new status", insertedStatus)
    }

    @PostMapping
    fun update(@RequestBody status: Status): Response<Status> {
        val savedStatus = repository.save(status)
        return response("Succesfully updated status",  savedStatus)
    }
}