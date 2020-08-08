package com.dubedivine.apps.yerrr.model.responseEntity

/**
 * Contract
 * status is true if there is no message and there is an entity
 * status false if there is no entity and there is a message
* */
class StatusResponseEntity<T>(
        var status: Boolean,
        var message: String,
        var entity: T? = null
)