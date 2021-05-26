package com.example.slotmachine4.game

import android.content.SharedPreferences
import com.example.slotmachine4.game.Actions.INCREASE
import com.example.slotmachine4.game.Actions.REDUCE

enum class Actions {
    INCREASE, REDUCE
}

class Game (
        var pref: SharedPreferences
) {
    var rate = 1
    var userGold = 0

    fun getUserCash(): Int {
        userGold = pref.getInt(PrefsKeys.GOLD, 2500)
        return userGold
    }

    fun userAction(action: Actions) {
        when (action) {
            INCREASE -> {
                if (userGold > 0) {
                    rate++
                    userGold--
                }
            }
            REDUCE -> {
                if (rate > 0) {
                    rate--
                    userGold++
                }
            }
        }
    }

    fun spinResults(slotsImages: List<Int>) : String {
        when (slotsImages.distinct().count()) {
            1 -> {
                userGold += rate * 5
                saveResults()
                return PrefsKeys.BIG_PRIZE
            }
            2 -> {
                userGold += rate * 3
                saveResults()
                return PrefsKeys.MEDIUM_PRIZE
            }
            3 -> {
                userGold += rate * 3

                saveResults()
                return PrefsKeys.MEDIUM_PRIZE
            }
            4 -> {
                userGold += rate * 2
                saveResults()
                return PrefsKeys.SMALL_PRIZE
            }
            else -> {
                saveResults()
                return PrefsKeys.NO_PRIZE
            }
        }
    }

    private fun saveResults() {
        val editor = pref.edit()

        rate = 1
        userGold -= 1

        editor.putInt(PrefsKeys.GOLD, userGold)
        editor.apply()
    }
}