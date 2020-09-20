package com.dubedivine.apps.yerrr.repository.status

import com.dubedivine.apps.yerrr.model.requestObject.StatusLike
import com.dubedivine.apps.yerrr.model.requestObject.StatusVote
import com.dubedivine.apps.yerrr.repository.shared.VoteRepository

interface StatusLikeRepository: VoteRepository<StatusLike>