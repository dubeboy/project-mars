package com.dubedivine.apps.yerrr.model

import com.dubedivine.apps.yerrr.model.abstractEntity.Votable
import com.fasterxml.jackson.annotation.JsonIgnore
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import java.util.*

data class Comment(
        val body: String,
        override var user: User = User("2000", "Johnna", "@johnna_major_league", "09809876098", null),
        val media: Media? = null,
        val createdAt: Date = Date(),
        override var votes: Long,
        override var likes: Long,
        val location: GeoJsonPoint = GeoJsonPoint(0.0, 0.0), // TODO: change this
        @JsonIgnore
        var isDeleted: Boolean = false,
        val id: String = ObjectId().toHexString(),
        @JsonIgnore
        val flagged: Flag = Flag()
): Votable