package com.example.slotmachine4.game

import android.content.SharedPreferences

class Game (
        var pref: SharedPreferences
) {
    var rate = 1
    var userGold = 0

    fun getUserCash(): Int {
        userGold = pref.getInt(PrefsKeys.GOLD, 2500)
        return userGold
    }

    fun userAction(action: String) {
        when (action) {
            PrefsKeys.INCREASE -> {
                if (userGold > 0) {
                    rate++
                    userGold--
                }
            }
            PrefsKeys.REDUCE -> {
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
                saveResults(5)
                return PrefsKeys.BIG_PRIZE
            }
            2 -> {
                saveResults(4)
                return PrefsKeys.MEDIUM_PRIZE
            }
            3 -> {
                saveResults(3)
                return PrefsKeys.MINIMUM_PRIZE
            }
            4 -> {
                saveResults(2)
                return PrefsKeys.SMALL_PRIZE
            }
            else -> {
                saveResults(0)
                return PrefsKeys.NO_PRIZE
            }
        }
    }

    private fun saveResults(prizeMultiplier: Int) {
        val editor = pref.edit()

        userGold += rate * prizeMultiplier

        rate = 1
        userGold--

        editor.putInt(PrefsKeys.GOLD, userGold)
        editor.apply()
    }
}