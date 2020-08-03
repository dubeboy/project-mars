package com.dubedivine.apps.yerrr.model

import org.springframework.data.annotation.Id
import java.util.*

data class Comment(
        @Id
        val id: String,
        val name: String,
        val createdAt: Date = Date(),
        val body: String,
        val media: Media? = null
)