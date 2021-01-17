package com.dubedivine.apps.yerrr.controller.status

import com.dubedivine.apps.yerrr.controller.users.UsersController.Companion.USER_NOT_FOUND
import com.dubedivine.apps.yerrr.model.PeopleCircle
import com.dubedivine.apps.yerrr.model.Status
import com.dubedivine.apps.yerrr.model.requestObject.UserCircleRequestObject
import com.dubedivine.apps.yerrr.model.responseEntity.StatusResponseEntity
import com.dubedivine.apps.yerrr.repository.circle.CircleRepositoryImplementation
import com.dubedivine.apps.yerrr.repository.user.UserRepository
import com.dubedivine.apps.yerrr.utils.Response
import com.dubedivine.apps.yerrr.utils.response
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("circles")
class CirclesController(private val circlesRepository: CircleRepositoryImplementation,
                        private val userRepository: UserRepository) {


    @GetMapping
    fun getAllCircles(): Response<List<PeopleCircle>> {
        val circles = circlesRepository.getAllCircles()
        return response("", circles)
    }

//    @PutMapping // no exposed for now!!!
    fun create(@RequestBody circle: PeopleCircle) {
        circlesRepository.create(circle)
    }

    @GetMapping("statuses")
    fun getStatusesForCircles(@RequestParam("circle_name") circleName: String,
                              @RequestParam page: Int? = 0): Response<List<Status>> {
        val circles = circlesRepository.getStatusesForCircle(circleName, page ?: 0 )
        return response("", circles)
    }


    @PostMapping("join")
    fun joinCircle(@RequestBody requestObject: UserCircleRequestObject): Response<Boolean> {
        val user = userRepository.findByIdOrNull(requestObject.userId)
        val circle = circlesRepository.findByIdOrNull(requestObject.circleName)
        return when {
            user != null && circle != null -> {
                val wasAck = circlesRepository.joinCircle(circle, user)
                response(String.format(JOIN_SUCCESS, requestObject.circleName), true)
            }
            else -> {
                response(String.format(JOIN_FAIL, requestObject.circleName), false)
            }
        }
    }

    @GetMapping("my_circles")
    fun getMyCircles(@RequestParam("user_id") userId: String): Response<List<PeopleCircle>>  {
        val user = userRepository.findByIdOrNull(userId)
        return when {
            user != null -> {
                val circles = circlesRepository.getUserCircles(user)
                response(if(circles == null) NULL_CIRCLES_RETURED else "", circles)
            }
            else -> {
                response(USER_NOT_FOUND, null)
            }
        }
    }

    companion object {
        const val JOIN_SUCCESS = "%s is now in your circles"
        const val JOIN_FAIL = "Sorry could not add %s to your circles"
        const val NULL_CIRCLES_RETURED = "Sorry could not add %s to your circles"
    }

}