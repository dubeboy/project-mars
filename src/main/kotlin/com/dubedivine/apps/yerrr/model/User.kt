package com.dubedivine.apps.yerrr.model

data class User (
    val name: String,
    val media: Media,
    val statuses: List<User> = emptyList(),
    val point: Point?
)
