package com.dubedivine.apps.yerrr.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class PeopleCircle(@Id @Indexed(unique = true) val name: String,
                        var description: String? = null,
                        val users: List<User> = emptyList(),
                        val statuses: List<Status>)
// add isSubsscribed???