package com.dubedivine.apps.yerrr.model

import com.dubedivine.apps.yerrr.model.abstractEntity.Votable
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import kotlin.collections.ArrayList

// TODO: add a report flag to this later

@Document
data class Status(
        val body: String = "",
        override var user: User,
        @Id
        var id: String? = null,
        @JsonIgnore
        var comments: ArrayList<Comment> = java.util.ArrayList(),
        @JsonIgnore
        var isDeleted: Boolean = false,
        val geoLocation: Double = 0.0, // TODO: use MONGO geolocation here
        var media: ArrayList<Media> = ArrayList(),
        override var likes: Long = 0, // A positive integer
        override var votes: Long = 0, // negative and positive integer
        val createdAt: Date = Date()
): Votable