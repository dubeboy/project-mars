package com.dubedivine.apps.yerrr.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

enum class MediaType {
    VIDEO, PICTURE
}

data class Media (
    @Id
    var name: String,
    var type: Int,
    var location: String,
    val createAt: Date = Date()
) {
    var size: Long? = null
}