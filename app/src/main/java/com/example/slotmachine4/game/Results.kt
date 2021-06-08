package com.example.slotmachine4.game

import android.content.SharedPreferences
import com.example.slotmachine4.preferences.PrefsKeys
import com.example.slotmachine4.preferences.PrefsKeysPrizes

class Results  {
    fun spinResults(
            slotsImages: List<Int>,
            game: Game,
            pref: SharedPreferences,
            statistics: Statistics
    ) : String {
        when (slotsImages.distinct().count()) {
            1 -> {
                saveResults(5, game, pref)
                statistics.x5Prizes++
                return PrefsKeysPrizes.BIG_PRIZE
            }
            2 -> {
                saveResults(4, game, pref)
                statistics.x4Prizes++
                return PrefsKeysPrizes.MEDIUM_PRIZE
            }
            3 -> {
                saveResults(3, game, pref)
                statistics.x3Prizes++
                return PrefsKeysPrizes.MINIMUM_PRIZE
            }
            4 -> {
                saveResults(2, game, pref)
                statistics.x2Prizes++
                return PrefsKeysPrizes.SMALL_PRIZE
            }
            else -> {
                saveResults(0, game, pref)
                return PrefsKeysPrizes.NO_PRIZE
            }
        }
    }

    private fun saveResults(prizeMultiplier: Int, game: Game, pref: SharedPreferences) {
        val editor = pref.edit()

        game.userGold += game.rate * prizeMultiplier

        game.rate = 1
        game.userGold--

        editor.putInt(PrefsKeys.GOLD, game.userGold)
        editor.apply()
    }
}