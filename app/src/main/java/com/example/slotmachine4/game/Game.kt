package com.example.slotmachine4.game

import android.widget.ImageView
import com.example.slotmachine4.MainActivity
import com.example.slotmachine4.R.drawable.*
import com.example.slotmachine4.preferences.PrefsKeysActions
import java.util.*
import kotlin.random.Random

class Game {
    var rate = 1
    var userGold = 0

    fun userAction(action: String) {
        when (action) {
            PrefsKeysActions.INCREASE -> {
                if (userGold > 0) {
                    rate++
                    userGold--
                }
            }
            PrefsKeysActions.REDUCE -> {
                if (rate > 0) {
                    rate--
                    userGold++
                }
            }
        }
    }

    fun setImagesInSlots(): ArrayList<MutableList<Int>> {
        val imagesInSlots = arrayListOf<MutableList<Int>>()

        val images = mutableListOf(
                icon1, icon2, icon3, icon4, icon5,
                icon6, icon7, icon8, icon9, icon10
        )

        images.shuffle()

        for (i in 0 until 5)
            imagesInSlots.add(images)

        return imagesInSlots
    }

    fun startSpin(
            slot: ImageView,
            imagesArray: MutableList<Int>,
            from: Long,
            until: Long,
            activity: MainActivity
    ): Slots {
        val slotImagesSpin = Slots(object : Slots.SpinListener {
            override fun newImage(img: Int) {
                activity.runOnUiThread {
                    slot.setImageResource(img)
                }
            }
        }, imagesArray, Random.nextLong(from, until))
        slotImagesSpin.start()

        return slotImagesSpin
    }
}