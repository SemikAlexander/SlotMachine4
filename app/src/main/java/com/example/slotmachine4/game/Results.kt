package com.example.slotmachine4.game

import android.content.SharedPreferences

class Results (
        var pref: SharedPreferences
) {
    fun spinResults(slotsImages: List<Int>, game: Game) : String {
        when (slotsImages.distinct().count()) {
            1 -> {
                saveResults(5, game)
                return PrefsKeys.BIG_PRIZE
            }
            2 -> {
                saveResults(4, game)
                return PrefsKeys.MEDIUM_PRIZE
            }
            3 -> {
                saveResults(3, game)
                return PrefsKeys.MINIMUM_PRIZE
            }
            4 -> {
                saveResults(2, game)
                return PrefsKeys.SMALL_PRIZE
            }
            else -> {
                saveResults(0, game)
                return PrefsKeys.NO_PRIZE
            }
        }
    }

    fun saveResults(prizeMultiplier: Int, game: Game) {
        val editor = pref.edit()

        game.userGold += game.rate * prizeMultiplier

        game.rate = 1
        game.userGold--

        editor.putInt(PrefsKeys.GOLD, game.userGold)
        editor.apply()
    }
}