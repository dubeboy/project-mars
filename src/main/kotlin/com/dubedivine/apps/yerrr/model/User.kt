package com.dubedivine.apps.yerrr.model

import com.mongodb.lang.Nullable
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

// Could also add the ability to sign in using email
@Document
data class User(
        @Id val id: String,
        val name: String,
        @Indexed(unique = true)
        val handle: String, // Might serve as a handle @something_spicy
        @Indexed(unique = true)
        val phoneNumber: String, // might want to have this be a computed variable of PhoneNumber
        val profile: Media?,
        val statuses: List<User> = emptyList(),
        val point: Point?
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


