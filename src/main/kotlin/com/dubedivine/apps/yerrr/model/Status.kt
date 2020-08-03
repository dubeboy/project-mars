package com.dubedivine.apps.yerrr.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

// TODO: add a flag to this later

@Document
data class Status(
        @Id
        val id: String,
        val body: String,
        val user: User,
        val media: List<Media>? = null,
        val createdAt: Date,
        var comments: List<Comment>,
        var isDeleted: Boolean,
        val distanceFromYouval: Double // TODO: use MONGO geolocation here
)