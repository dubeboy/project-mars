package com.dubedivine.apps.yerrr.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.rmi.dgc.Lease

// Could also add the ability to sign in using email
@Document
data class User(
        @Id val id: String, // should change to object ID
        val name: String,
        @Indexed(unique = true)
        val handle: String, // Might serve as a handle @something_spicy
        @Indexed(unique = true)
        val phoneNumber: String, // might want to have this be a computed variable of PhoneNumber
        val profile: Media?,
        val statuses: List<User> = emptyList(), // put this last please!!!
        val point: Point?  // please change these
)

enum class CountryCode(val countryCode: String) {
    ZA("+27")
}

enum class Badges(s: String) {
    STRANGER("Stranger"),
    NEW_COMER("New Comer"),
    REGULAR("Regular"),
    GUIDE("Guide"),
    EXPERT("Expert"),
    LEADER("LEADER");

    fun level(level: Int): Badges {
        return when (level) {
            in 0..100 -> {
                STRANGER
            }
            in 100..200 -> {
                NEW_COMER
            }
            in 200..400 -> {  // You stay a regular for a while
                REGULAR
            }
            in 400..600 -> {
                GUIDE
            }
            in 600..800 -> {
                EXPERT
            }
            else -> { // 900 going up
                LEADER
            }
        }
    }
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


