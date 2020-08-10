package com.dubedivine.apps.yerrr.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

enum class MediaType {
    VIDEO, PICTURE
}

data class Media (
    var name: String,
    var type: Int,
    var location: String,
    val createAt: Date = Date(),
    @JsonIgnore
    var isDeleted: Boolean = false
) {
    var size: Long? = null
}