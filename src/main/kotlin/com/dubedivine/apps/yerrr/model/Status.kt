package com.dubedivine.apps.yerrr.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import kotlin.collections.ArrayList

// TODO: add a report flag to this later

@Document
data class Status(
        val body: String = "",
        var user: User = User("1000", "Booty", "@bootynizer", "0891231234", null, emptyList(), null),
        @Id
        var id: String? = null,
//        @JsonIgnore
        var comments: ArrayList<Comment> = java.util.ArrayList(),
        var isDeleted: Boolean = false,
        val geoLocation: Double = 0.0, // TODO: use MONGO geolocation here
        var media: List<Media>? = null,
        var likes: Int = 0, // A positive integer
        var votes: Int = 10, // negative and positive integer
        val createdAt: Date = Date()
)