package com.dubedivine.apps.yerrr.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

enum class MediaType {
    VIDEO, IMAGE
}

data class Media (
    @Id
    var name: String,
    val size: Long,
    var type: MediaType,
    var location: String,
    val createAt: Date = Date()
)