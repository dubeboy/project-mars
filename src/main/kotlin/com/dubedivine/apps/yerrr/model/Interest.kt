package com.dubedivine.apps.yerrr.model

import org.springframework.data.mongodb.core.mapping.Document

// These are community interest that you might be interested in!!!
@Document
data class Interest(
        val name: String,
        val media: Media? = null, // TODO: for future use probably, for now it will bve generated server side
        var statuses: List<Status> = emptyList()
        // probably how people are interested in this interest!!!
)