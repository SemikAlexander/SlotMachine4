package com.example.slotmachine4.game

import android.content.SharedPreferences
import com.example.slotmachine4.preferences.PrefsKeys

class Statistics {
    var x5Prizes = 0
    var x4Prizes = 0
    var x3Prizes = 0
    var x2Prizes = 0
    var spinCount = 0

    fun saveStatInfo(
            pref: SharedPreferences
    ) {
        val editor = pref.edit()

        editor.putInt(PrefsKeys.BIG_PRIZES_COUNT, x5Prizes)
        editor.putInt(PrefsKeys.MEDIUM_PRIZES_COUNT, x4Prizes)
        editor.putInt(PrefsKeys.MINIMUM_PRIZES_COUNT, x3Prizes)
        editor.putInt(PrefsKeys.SMALL_PRIZES_COUNT, x2Prizes)

        editor.putInt(PrefsKeys.SPIN_COUNT, spinCount)

        editor.apply()
    }

    fun getStatInfo(pref: SharedPreferences): List<Int> {
        return listOf(
                pref.getInt(PrefsKeys.GOLD, 0),
                pref.getInt(PrefsKeys.BIG_PRIZES_COUNT, 0),
                pref.getInt(PrefsKeys.MEDIUM_PRIZES_COUNT, 0),
                pref.getInt(PrefsKeys.MINIMUM_PRIZES_COUNT, 0),
                pref.getInt(PrefsKeys.SMALL_PRIZES_COUNT, 0),
                pref.getInt(PrefsKeys.SPIN_COUNT, 0)
        )
    }

    fun clearStatistic(pref: SharedPreferences) {
        x5Prizes = 0
        x4Prizes = 0
        x3Prizes = 0
        x2Prizes = 0
        spinCount = 0

        saveStatInfo(pref)
    }
}