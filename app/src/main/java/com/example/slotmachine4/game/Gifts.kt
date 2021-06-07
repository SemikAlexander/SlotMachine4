package com.example.slotmachine4.game

import android.annotation.SuppressLint
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

class Gifts(
        var pref: SharedPreferences
) {
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)

    @SuppressLint("SimpleDateFormat")
    fun everydayGift(): Int {
        val lastEntryUnix = pref.getLong(PrefsKeys.ENTRY_DAY, 0)
        val currentDay = SimpleDateFormat("dd.MM.yyyy").parse(sdf.format(Date()))

        val editor = pref.edit()
        editor.putLong(PrefsKeys.ENTRY_DAY, (currentDay.time / 1000))

        if (lastEntryUnix == 0L) {
            editor.putInt(
                    PrefsKeys.GOLD,
                    (pref.getInt(PrefsKeys.GOLD, 250) + PrefsKeys.EVERYDAY_GOLD_GIFT)
            )
            editor.apply()
            return 10
        }

        val lastEntryDay = SimpleDateFormat("dd.MM.yyyy").parse(sdf.format(Date(lastEntryUnix * 1000)))

        return when (((currentDay.time - lastEntryDay.time) / (24 * 60 * 60 * 1000)).toInt()) {
            1 -> {
                editor.putInt(
                        PrefsKeys.GOLD,
                        (pref.getInt(PrefsKeys.GOLD, 250) + PrefsKeys.EVERYDAY_GOLD_GIFT)
                )
                editor.apply()
                10
            }
            else -> {
                editor.apply()
                0
            }
        }
    }

    fun makeGiftForUser() {
        val editor = pref.edit()

        editor.putInt(
                PrefsKeys.GOLD,
                (pref.getInt(PrefsKeys.GOLD, 250) + PrefsKeys.GOLD_GIFT)
        )
        editor.apply()
    }
}