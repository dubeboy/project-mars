package com.dubedivine.apps.yerrr.model.responseEntity

class StatusResponseEntity<T>(
        var status: Boolean,
        var message: String,
        var entity: T? = null
)