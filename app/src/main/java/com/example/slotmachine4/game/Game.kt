package com.example.slotmachine4.game

import android.content.SharedPreferences
import com.example.slotmachine4.R.drawable.*
import com.example.slotmachine4.game.Actions.*

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

    fun createImageSlotSequence(): MutableList<Int> {
        val images = intArrayOf (
                icon1, icon2, icon3, icon4, icon5,
                icon6, icon7, icon8, icon9, icon10
        ).toMutableList()

        images.shuffle()

        return images
    }

    fun spinResults(slotsImages: List<Int>) : String {
        when (slotsImages.distinct().count()) {
            1 -> {
                userGold += rate * 5

                rate = 1
                userGold -= 1

                saveResults()
                return PrefsKeys.BIG_PRIZE
            }
            2 -> {
                userGold += rate * 3

                rate = 1
                userGold -= 1

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

                rate = 1
                userGold -= 1

                saveResults()
                return PrefsKeys.SMALL_PRIZE
            }
            else -> {
                rate = 1
                userGold -= 1

                saveResults()
                return PrefsKeys.NO_PRIZE
            }
        }
    }

    private fun saveResults() {
        val editor = pref.edit()

        editor.putInt(PrefsKeys.GOLD, userGold)
        editor.apply()
    }
}