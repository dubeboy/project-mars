package com.dubedivine.apps.yerrr.model

import com.dubedivine.apps.yerrr.model.requestObject.EntityID
import com.dubedivine.apps.yerrr.model.requestObject.UserID
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class UserEntityID(val userId: UserID, val entityId: EntityID)