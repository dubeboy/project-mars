package com.dubedivine.apps.yerrr.model.abstractEntity

import com.dubedivine.apps.yerrr.model.UserEntityID

interface Vote {
    var id: UserEntityID
    var direction: Boolean
    var isDeleted: Boolean
    var valueWhenVoted: Long

    fun sanitize() {
        isDeleted = false
        valueWhenVoted = 0
    }
}