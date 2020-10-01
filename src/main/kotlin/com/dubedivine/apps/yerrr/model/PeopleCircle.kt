package com.dubedivine.apps.yerrr.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class PeopleCircle(@Id @Indexed(unique = true) val name: String,
                        val description: String,
                        val users: List<User>)