package com.dubedivine.apps.yerrr.model

import com.mongodb.lang.NonNull
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

typealias UserID = String
typealias StatusID = String

data class UserStatusID(val userId: UserID, val statusId: StatusID)

// isDeleted is important because want to be able to see how undecided people are
@Document
data class Vote(@Id @Indexed(unique = true) var id: UserStatusID, @NonNull var direction: Boolean = false, var isDeleted: Boolean = false) {
    var valueWhenVoted: Int = 0
}
