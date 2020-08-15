package com.dubedivine.apps.yerrr.model

import com.dubedivine.apps.yerrr.model.abstractEntity.Vote
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed

data class StatusLike(@Id @Indexed(unique = true) override var id: UserEntityID,
                      override var direction: Boolean = true,
                      @JsonIgnore
                      override var isDeleted: Boolean = false,
                      override var valueWhenVoted: Long = 0) : Vote

typealias CommentLike = StatusLike