package com.dubedivine.apps.yerrr.model.abstractEntity

import com.dubedivine.apps.yerrr.model.User


interface Votable {
    var votes: Long
    var likes: Long
    var user: User
}