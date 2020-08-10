package com.dubedivine.apps.yerrr.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

// TODO: add a report flag to this later

@Document
data class Status(
        val body: String,
        var user: User = User("1000", "Booty", "@bootynizer", "0891231234", null, emptyList(), null),
        @Id
        var id: String? = null,
        var comments: List<Comment> = emptyList(),
        var isDeleted: Boolean = false,
        val geoLocation: Double = 0.0, // TODO: use MONGO geolocation here
        var media: List<Media>? = null,
        var likes: Int = 0, // A positive integer
        var votes: Int = 0, // negative and positive integer
        val createdAt: Date = Date()
)