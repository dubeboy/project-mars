package com.dubedivine.apps.yerrr.model

import org.springframework.data.mongodb.core.mapping.Document

enum class Badges(val string: String) {
    STRANGER("Stranger"),
    NEW_COMER("New Comer"),
    REGULAR("Regular"),
    GUIDE("Guide"),
    EXPERT("Expert"),
    LEADER("Leader")
}

@Document
data class Point(
        var score: Int = 0,
        var badge: String = Badges.STRANGER.string
) {

    fun setScoreBadges(value: Int) {
        badge = level(value).string
        score += value
    }



    private fun level(level: Int): Badges {
        return when (level) {
            in 0..100 -> {
                Badges.STRANGER
            }
            in 100..200 -> {
                Badges.NEW_COMER
            }
            in 200..400 -> {  // You stay a regular for a while 200
                Badges.REGULAR
            }
            in 400..700 -> { // 300
                Badges.GUIDE
            }
            in 700..1100 -> { // 400
                Badges.EXPERT
            }
            else -> { // 900 going up
                Badges.LEADER
            }
        }
    }
}


/**
 * rules
 * User            | Comment        |  Status
 * ----------------|----------------|-----------------
 * Voter           | +0             | +0
 * Owner           | +1  √          | +1 √
 *
* */