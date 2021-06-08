package com.example.slotmachine4.game

import android.content.SharedPreferences
import com.example.slotmachine4.preferences.PrefsKeys
import com.example.slotmachine4.preferences.PrefsKeysPrizes

class Statistics {
    var x5Prizes = 0
    var x4Prizes = 0
    var x3Prizes = 0
    var x2Prizes = 0
    var spinCount = 0

    fun saveStatInfo(pref: SharedPreferences) {
        val editor = pref.edit()

        editor.putInt(PrefsKeysPrizes.BIG_PRIZES_COUNT, x5Prizes)
        editor.putInt(PrefsKeysPrizes.MEDIUM_PRIZES_COUNT, x4Prizes)
        editor.putInt(PrefsKeysPrizes.MINIMUM_PRIZES_COUNT, x3Prizes)
        editor.putInt(PrefsKeysPrizes.SMALL_PRIZES_COUNT, x2Prizes)

        editor.putInt(PrefsKeys.SPIN_COUNT, spinCount)

        editor.apply()
    }

    fun getStatInfo(pref: SharedPreferences): List<Int> {
        return listOf(
                pref.getInt(PrefsKeys.GOLD, 0),
                pref.getInt(PrefsKeysPrizes.BIG_PRIZES_COUNT, 0),
                pref.getInt(PrefsKeysPrizes.MEDIUM_PRIZES_COUNT, 0),
                pref.getInt(PrefsKeysPrizes.MINIMUM_PRIZES_COUNT, 0),
                pref.getInt(PrefsKeysPrizes.SMALL_PRIZES_COUNT, 0),
                pref.getInt(PrefsKeys.SPIN_COUNT, 0)
        )
    }

    fun setValues(pref: SharedPreferences) {
        val values = getStatInfo(pref)

        x5Prizes = values[1]
        x4Prizes = values[2]
        x3Prizes = values[3]
        x2Prizes = values[4]
        spinCount = values[5]
    }

    fun clearStatistic(pref: SharedPreferences) {
        x5Prizes = 0
        x4Prizes = 0
        x3Prizes = 0
        x2Prizes = 0
        spinCount = 0

        saveStatInfo(pref)
    }

    fun thanksMessage(): String {
        when (spinCount) {
            10 -> return "$spinCount spins! Thank you for long play!"
            20 -> return "$spinCount spins! You're play is good motivation for us! Thank you!!!"
            50 -> return "$spinCount spins! Thank you for so long play in our game!"
            100 -> return "$spinCount spins!! You're the best!"
            200 -> return "$spinCount spins!!! You're really the best!! We are very grateful to you!"
            300 -> return "$spinCount spins!!!! You're truly the best in whole world! Thank you so much!!!"
        }
        return ""
    }
}