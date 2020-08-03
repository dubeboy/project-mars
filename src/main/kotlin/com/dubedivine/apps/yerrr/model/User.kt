package com.dubedivine.apps.yerrr.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(
        @Id val id: String,
        val name: String,
        val phoneNumber: String,
        val media: Media,
        val statuses: List<User> = emptyList(),
        val point: Point?
)
