package com.dubedivine.apps.yerrr.model

import org.springframework.data.mongodb.core.mapping.Document

enum class Type {
    STRANGER, NEW_COMER, REGULAR, GUIDE, EXPERT, LEADER
}

@Document
data class Point(
        val type: Type,
        val scores: List<Int>
)