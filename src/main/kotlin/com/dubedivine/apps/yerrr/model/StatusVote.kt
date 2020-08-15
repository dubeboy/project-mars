package com.dubedivine.apps.yerrr.model

import com.dubedivine.apps.yerrr.model.abstractEntity.Vote
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

typealias UserID = String
typealias EntityID = String

// isDeleted is important because want to be able to see how undecided people are
@Document
data class StatusVote(@Id @Indexed(unique = true) override var id: UserEntityID,
                      override var direction: Boolean,
                      @JsonIgnore override var isDeleted: Boolean = false): Vote {
    override var valueWhenVoted: Long = 0
}

