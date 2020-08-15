package com.dubedivine.apps.yerrr.controller.users

import com.dubedivine.apps.yerrr.repository.UserRepository
import com.dubedivine.apps.yerrr.model.PhoneNumber
import org.springframework.web.bind.annotation.*
import com.dubedivine.apps.yerrr.model.User
import com.dubedivine.apps.yerrr.model.responseEntity.StatusResponseEntity
import com.dubedivine.apps.yerrr.utils.KUtils
import com.dubedivine.apps.yerrr.utils.response
import org.springframework.http.ResponseEntity

/**
 * Goals:
 *  1) Have a central user with Peerlink and Yerr
 *  2) Utilises firebase for all logins so we do not need to validate the number
 * */

@RestController
@RequestMapping("users")
class UsersController(private val userRepository: UserRepository) {

    /**
     * This will create a new user and sign or return a status that this use does not exist
     * If the user is logging in using phone number we should not allow them to have more than one session, we should remove the other session
     * after receiving the code: https://firebase.google.com/docs/auth/android/phone-auth
    * */
    @PostMapping
    fun signIn(@RequestBody user: User): ResponseEntity<StatusResponseEntity<User>> {
        val userExists = userRepository.existsByPhoneNumber(user.phoneNumber) // should also check email later
        return when {
            userExists -> {
                response(USER_EXISTS_MESSAGE, userRepository.findByPhoneNumber(user.phoneNumber))
            }
            else -> {
                // we inform the client that they need to create a new user
//                return if (isValidPhoneNumber(user.phoneNumber)) {
                    response(USER_REGISTRATION_SUCCESSFUL, userRepository.save(user))
//                } else {

//                }
            }
        }
    }

    // when we check if the user exists we either return a user o nothing
    // NB: this must be super light weight
    @GetMapping("user_exists")
    fun userExists(@RequestParam("phone_number") phoneNumber: String): ResponseEntity<StatusResponseEntity<User>> {
        val userExists = userRepository.existsByPhoneNumber(phoneNumber)
        return when {
            userExists -> {
                response(USER_EXISTS_MESSAGE, userRepository.findByPhoneNumber(phoneNumber))
            }
            else -> {
                response(USER_DOES_NOT_EXIST, null, false)
            }
        }
    }


    // This is a bit a vague
    private fun isValidPhoneNumber(phoneNumber: PhoneNumber) =
            phoneNumber.phoneNumber.length == KUtils.correctPhoneNumberLength(phoneNumber)
    /**
     * This is so that when we want to centralise all the error messages to one library we can then do so
     * 2) provide meaningful errors
     * */
    companion object {
        const val USER_EXISTS_MESSAGE = "Signing you in..."
        const val USER_REGISTRATION_SUCCESSFUL = "Successfully registered"
        const val USER_DOES_NOT_EXIST = "Please create a new user"
    }

    fun calculateUserPoints() {

    }
}