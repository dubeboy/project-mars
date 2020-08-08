package com.dubedivine.apps.yerrr.controller

import com.dubedivine.apps.yerrr.Repository.StatusRepository
import com.dubedivine.apps.yerrr.Repository.UserRepository
import com.dubedivine.apps.yerrr.model.Status
import com.dubedivine.apps.yerrr.model.responseEntity.StatusResponseEntity
import com.dubedivine.apps.yerrr.utils.KUtils
import com.dubedivine.apps.yerrr.utils.Response
import com.dubedivine.apps.yerrr.utils.createdResponse
import com.dubedivine.apps.yerrr.utils.response
import org.springframework.data.mongodb.gridfs.GridFsOperations
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("statuses")
class StatusesController(private val userRepository: UserRepository,
                         private val repository: StatusRepository,
                         private val gridFSOperations: GridFsOperations) {

    @GetMapping
    fun all(): Response<List<Status>> {
        // TODO: Order by geolocation
        return response("", repository.findAll())
    }

    // TODO: maybe create a handshake JWT everytime a user is posting to verify device, we could use FCM
    @PutMapping
    fun create(@RequestBody status: Status): Response<Status> {
//        val user = userRepository.findByIdOrNull(status.user.id)
//                ?: return badRequestResponse("Something went wrong, please try logging in again", null)
//        status.user = user
        val insertedStatus = repository.insert(status)
        return createdResponse("Successfully posted new status", insertedStatus)
    }

    private fun createHandle(handle: String): String {
        return "@$handle"
    }

    // Check for errasure
    @PostMapping
    fun update(@RequestBody status: Status): Response<Status> {
        val savedStatus = repository.save(status)
        return response("Succesfully updated status",  savedStatus)
    }

    @PostMapping("{status_id}/files")
    fun addFiles(@PathVariable("status_id") statusId: String,
                 @RequestPart files: List<MultipartFile>): ResponseEntity<StatusResponseEntity<Status>> {
        return when (val status = repository.findByIdOrNull(statusId)) {
            null -> {
                response(CANNOT_FIND_STATUS, null, false, HttpStatus.NOT_FOUND)
            }
            else -> {
                val media = KUtils.createMedia(files, gridFSOperations)
                status.media = media
                response(MEDIA_SAVED, repository.save(status))
            }
        }

    }

    companion object {
        const val CANNOT_FIND_STATUS = "Sorry could not add files because we could not find that Status"
        const val MEDIA_SAVED = "successfully posted media"
    }

}