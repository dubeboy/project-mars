package com.dubedivine.apps.yerrr.model

import com.dubedivine.apps.yerrr.model.abstractEntity.Votable
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import kotlin.collections.ArrayList

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
        val location: Location = Location(0.0, 0.0),
        var media: ArrayList<Media> = ArrayList(),
        override var likes: Long = 0, // A positive integer
        override var votes: Long = 0, // negative and positive integer
        var createdAt: Date = Date(),
        @JsonIgnore
        var flagged: Flag = Flag()
): Votable {
        @JsonIgnore
        var geoLocation: GeoJsonPoint = GeoJsonPoint(location.x, location.y)

        fun sanitizeStatus() {
                id = null
                comments = ArrayList()
                isDeleted = false
                likes = 0
                votes = 0
                createdAt = Date()
                flagged = Flag()
        }
}