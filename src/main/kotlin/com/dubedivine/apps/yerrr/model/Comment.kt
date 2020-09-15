package com.dubedivine.apps.yerrr.model

import com.dubedivine.apps.yerrr.model.abstractEntity.Votable
import com.fasterxml.jackson.annotation.JsonIgnore
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import java.util.*

data class Comment(
        val body: String,
        override var user: User = User("2000", "Johnna", "@johnna_major_league", "09809876098", null),
        val media: List<Media> = emptyList(),
        var createdAt: Date = Date(),
        override var votes: Long,
        override var likes: Long,
        val location: Location = Location(0.0, 0.0),
        @JsonIgnore
        var isDeleted: Boolean = false,
        var id: String = ObjectId().toHexString(),
        @JsonIgnore
        val flagged: Flag = Flag()
): Votable {

    @JsonIgnore
    var geoLocation: GeoJsonPoint = GeoJsonPoint(this.location.x, this.location.y)

    // clean the model from outside impurities, haaaaaa!!!!
    fun sanitizeModel() {
        createdAt = Date()
        votes = 0
        likes = 0
        isDeleted = false
        id = ObjectId().toHexString()
    }
}

data class Location(val x: Double, val y: Double)

/**
 * location is stored in GeoJSON format.
 * {
 *   "type" : "Point",
 *   "coordinates" : [ x, y ]
 * }
 */