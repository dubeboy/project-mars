package com.dubedivine.apps.yerrr.utils

import com.dubedivine.apps.yerrr.model.responseEntity.StatusResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

private fun <T> response(status: Boolean, httpStatus: HttpStatus, message: String, entity: T?): ResponseEntity<StatusResponseEntity<T>> {
    // TODO: Add some statistic here so that we can see how many og these are failing and why
    return ResponseEntity(
            StatusResponseEntity(status,
                    message, entity), httpStatus
    )
}

fun <T> badRequestResponse(message: String, entity: T?): ResponseEntity<StatusResponseEntity<T>> {
    return response(
            false,
            HttpStatus.BAD_REQUEST,
            message,
            entity
    )
}

fun <T> createdResponse(message: String, entity: T?): ResponseEntity<StatusResponseEntity<T>> {
    return response(
            true,
            HttpStatus.CREATED,
            message,
            entity
    )
}