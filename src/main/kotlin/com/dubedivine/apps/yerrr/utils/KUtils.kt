package com.dubedivine.apps.yerrr.utils

import com.dubedivine.apps.yerrr.model.Status
import com.dubedivine.apps.yerrr.model.responseEntity.StatusResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

typealias Response<T> = ResponseEntity<StatusResponseEntity<T>>

fun <T> response(message: String, entity: T?, status: Boolean = true, httpStatus: HttpStatus = HttpStatus.OK): ResponseEntity<StatusResponseEntity<T>> {
    // TODO: Add some statistic here so that we can see how many og these are failing and why
    return ResponseEntity(
            StatusResponseEntity(status,
                    message, entity), httpStatus
    )
}

fun <T> badRequestResponse(message: String, entity: T?): ResponseEntity<StatusResponseEntity<T>> {
    return response(
            message,
            entity,
            false,
            HttpStatus.BAD_REQUEST
    )
}

fun <T> createdResponse(message: String, entity: T?): ResponseEntity<StatusResponseEntity<T>> {
    return response(
            message,
            entity,
            true,
            HttpStatus.CREATED
    )
}