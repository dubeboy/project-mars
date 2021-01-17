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
        var badge: String = Badges.STRANGER.string,
        var colorHex: String = hexColors[0]
) {

    fun setScoreBadges(value: Int) {
        badge = level(value).first.string
        score += value
        colorHex =  level(value).second
    }



    private fun level(level: Int): Pair<Badges, String>  {
        return when (level) {
            in 0..100 -> {
                Pair(Badges.STRANGER, hexColors[0])
            }
            in 100..200 -> {
                Pair(Badges.NEW_COMER, hexColors[1])
            }
            in 200..400 -> {  // You stay a regular for a while 200
                Pair(Badges.REGULAR, hexColors[2])
            }
            in 400..700 -> { // 300
                Pair(Badges.GUIDE, hexColors[3])
            }
            in 700..1100 -> { // 400
                Pair(Badges.EXPERT, hexColors[4])
            }
            else -> { // 900 going up
                Pair(Badges.LEADER, hexColors[5])
            }
        }
    }


    companion object {
        var hexColors = arrayOf("264653","2a9d8f","e9c46a","f4a261","e76f51", "fca311")
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