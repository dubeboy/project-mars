package com.dubedivine.apps.yerrr.repository.user

import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImplementation(private val userRepository: UserRepository) {

    // when the user is deleted also updated the corresponding circles
    // also when the profile is updated we should update circles
    // update when points is updates

    // try aggregation for Status to see which user liked which status
}