package com.dubedivine.apps.yerrr.model.abstractEntity

import com.dubedivine.apps.yerrr.model.UserEntityID
import org.springframework.data.mongodb.core.mapping.Document

@Document
interface Vote {
    var id: UserEntityID
    var direction: Boolean
    var isDeleted: Boolean
    var valueWhenVoted: Long


}