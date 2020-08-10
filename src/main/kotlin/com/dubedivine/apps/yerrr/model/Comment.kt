package com.dubedivine.apps.yerrr.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import java.util.*

data class Comment(
        val body: String,
        val user: User = User("2000", "Johnna", "@johnna_major_league", "09809876098", null, emptyList(), null),
        val media: Media? = null,
        val createdAt: Date = Date(),
        var isDeleted: Boolean = false,
        val id: String = ObjectId().toHexString()
)