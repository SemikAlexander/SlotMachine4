package com.example.slotmachine4.game

import android.annotation.SuppressLint
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class Game (
        var pref: SharedPreferences
) {
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)

    var rate = 1
    var userGold = 0

    fun getUserCash(): Int {
        userGold = pref.getInt(PrefsKeys.GOLD, 250)
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

    fun makeGiftForUser() {
        val editor = pref.edit()

        editor.putInt(PrefsKeys.GOLD, (getUserCash() + PrefsKeys.GOLD_GIFT))
        editor.apply()
    }

    @SuppressLint("SimpleDateFormat")
    fun everydayGift(): Int {
        val lastEntryUnix = pref.getLong(PrefsKeys.ENTRY_DAY, 0)
        val currentDay = SimpleDateFormat("dd.MM.yyyy").parse(sdf.format(Date()))

        val editor = pref.edit()
        editor.putLong(PrefsKeys.ENTRY_DAY, (currentDay.time / 1000))
        editor.putInt(PrefsKeys.GOLD, (getUserCash() + PrefsKeys.EVERYDAY_GOLD_GIFT))
        editor.apply()

        if (lastEntryUnix == 0L)
            return 10

        val lastEntryDay = SimpleDateFormat("dd.MM.yyyy").parse(sdf.format(Date(lastEntryUnix * 1000)))

        return when (((currentDay.time - lastEntryDay.time) / (24 * 60 * 60 * 1000)).toInt()) {
            1 -> 10
            else -> 0
        }
    }
}