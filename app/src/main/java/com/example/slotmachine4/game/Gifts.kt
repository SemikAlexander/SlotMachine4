package com.example.slotmachine4.game

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.example.slotmachine4.preferences.PrefsKeys
import com.example.slotmachine4.preferences.PrefsKeysPrizes
import java.text.SimpleDateFormat
import java.util.*

class Gifts(
        var pref: SharedPreferences
) {
    private val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.US)

    @SuppressLint("SimpleDateFormat")
    fun everydayGift(): Int {
        val lastEntryUnix = pref.getLong(PrefsKeys.ENTRY_DAY, 0)
        var countDayEntry = pref.getInt(PrefsKeys.COUNT_ENTRY_DAY, 0)

        val currentDay = SimpleDateFormat("dd.MM.yyyy").parse(sdf.format(Date()))

        val editor = pref.edit()
        editor.putLong(PrefsKeys.ENTRY_DAY, (currentDay.time / 1000))

        if (lastEntryUnix == 0L) {
            editor.putInt(
                    PrefsKeys.GOLD,
                    (pref.getInt(PrefsKeys.GOLD, 250) + PrefsKeysPrizes.EVERYDAY_GOLD_GIFT)
            )

            countDayEntry++
            editor.putInt(PrefsKeys.COUNT_ENTRY_DAY, countDayEntry)

            editor.apply()
            return 10
        }

        val lastEntryDay = SimpleDateFormat("dd.MM.yyyy").parse(sdf.format(Date(lastEntryUnix * 1000)))

        return when (((currentDay.time - lastEntryDay.time) / (24 * 60 * 60 * 1000)).toInt()) {
            1 -> {
                editor.putInt(
                        PrefsKeys.GOLD,
                        (pref.getInt(PrefsKeys.GOLD, 250) + PrefsKeysPrizes.EVERYDAY_GOLD_GIFT)
                )

                countDayEntry++
                editor.putInt(PrefsKeys.COUNT_ENTRY_DAY, countDayEntry)

                editor.apply()
                10
            }
            else -> {
                countDayEntry = 1
                editor.putInt(PrefsKeys.COUNT_ENTRY_DAY, countDayEntry)

                editor.apply()
                0
            }
        }
    }

    fun makeSaveGiftForUser() {
        val editor = pref.edit()

        editor.putInt(
                PrefsKeys.GOLD,
                (pref.getInt(PrefsKeys.GOLD, 250) + PrefsKeysPrizes.GOLD_GIFT)
        )
        editor.apply()
    }

    fun bigGift(): List<Int> {
        val editor = pref.edit()

        var countDaysEntry = pref.getInt(PrefsKeys.COUNT_ENTRY_DAY, 0)

        if (countDaysEntry == 7){
            countDaysEntry = 1
            editor.putInt(PrefsKeys.COUNT_ENTRY_DAY, countDaysEntry)
            editor.apply()
            val prize = mutableListOf(25, 50, 75, 100)
            prize.shuffle()

            return prize
        }
        return emptyList()
    }
}