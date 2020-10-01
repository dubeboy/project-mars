package com.dubedivine.apps.yerrr.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.rmi.dgc.Lease

// Could also add the ability to sign in using email
// will have foloowers in the future
@Document
data class User(
        @Id val id: String, // should change to object ID
        val name: String,
        @Indexed(unique = true)
        val handle: String, // Might serve as a handle @something_spicy
        @Indexed(unique = true)
        val phoneNumber: String, // might want to have this be a computed variable of PhoneNumber
        val profilePicture: Media?,
        val point: Point = Point(),
        @JsonIgnore
        var fcmToken: String? = null // I do this this is required
)

enum class CountryCode(val countryCode: String) {
    ZA("+27")
}

// this normalizes the phone number
data class PhoneNumber(val number: String,
                       val countryCode: String) {
    val phoneNumber: String = countryCode + number
    val code: CountryCode? = getCountryCode()

    private fun getCountryCode(): CountryCode? {
        return when (countryCode) {
            "ZA" -> {
                CountryCode.ZA
            }
            else -> {
                null
            }
        }
    }
}


