package com.example.slotmachine4

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.slotmachine4.databinding.ActivityMainBinding
import com.example.slotmachine4.game.Actions.*
import com.example.slotmachine4.game.Game
import com.example.slotmachine4.game.PrefsKeys
import com.example.slotmachine4.game.Slots
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random.Default.nextLong


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var pressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        val pref = getSharedPreferences(PrefsKeys.SETTING, Context.MODE_PRIVATE)
        val gameProcess = Game(pref)

        val slotsImages = listOf(
                gameProcess.createImageSlotSequence(), gameProcess.createImageSlotSequence(),
                gameProcess.createImageSlotSequence(), gameProcess.createImageSlotSequence(),
                gameProcess.createImageSlotSequence()
        )

        binding.apply {
            userGoldTextView.text = gameProcess.getUserCash().toString()
            userRate.text = gameProcess.rate.toString()

            increaseRate.setOnClickListener {
                gameProcess.userAction(INCREASE)
                setUserRateAndGold(gameProcess)
            }

            reduceRate.setOnClickListener {
                gameProcess.userAction(REDUCE)
                setUserRateAndGold(gameProcess)
            }

            spin.setOnClickListener {
                if (gameProcess.rate > 0) {
                    val slotsImagesSpin = arrayListOf<Slots>()

                    val slotsImagesView = listOf(slot1, slot2, slot3, slot4, slot5)

                    var from: Long = 0
                    var until: Long = 100

                    for (i in 0 until 5) {
                        slotsImagesSpin.add(doSpin(slotsImagesView[i], slotsImages[i], from, until))
                        from += 150
                        until += 200
                    }

                    GlobalScope.launch {
                        delay(6000)
                        launch(Dispatchers.Main) {
                            for (element in slotsImagesSpin)
                                element.stopSpin()

                            GlobalScope.launch {
                                delay(60)
                                launch(Dispatchers.Main) {

                                    val images: List<Int> = listOf(
                                            slotsImagesSpin[0].idImage,
                                            slotsImagesSpin[1].idImage,
                                            slotsImagesSpin[2].idImage,
                                            slotsImagesSpin[3].idImage,
                                            slotsImagesSpin[4].idImage
                                    )

                                    toast(gameProcess.spinResults(images))

                                    setSlotsImages(images)

                                    gameProcess.userGold -= 1
                                    setUserRateAndGold(gameProcess)

                                    gameProcess.rate = 1
                                    userRate.text = gameProcess.rate.toString()
                                }
                            }
                        }
                    }
                }
                else
                    toast("Enter the rate!")
            }
        }
    }

    override fun onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        }
        else
            toast("Press back again to exit")
        pressedTime = System.currentTimeMillis()
    }

    private fun setUserRateAndGold(gameProcess: Game) {
        binding.apply {
            userRate.text = gameProcess.rate.toString()
            userGoldTextView.text = gameProcess.userGold.toString()
        }
    }

    private fun doSpin(
            slot: ImageView,
            imagesArray: MutableList<Int>,
            from: Long,
            until: Long
    ): Slots {
        val slotImagesSpin = Slots(object : Slots.SpinListener {
            override fun newImage(img: Int) {
                runOnUiThread {
                    slot.setImageResource(img)
                }
            }
        }, imagesArray, nextLong(from, until))
        slotImagesSpin.start()

        return slotImagesSpin
    }

    private fun setSlotsImages(imagesArray: List<Int>) {
        binding.apply {
            slot1.setImageResource(imagesArray[0])
            slot2.setImageResource(imagesArray[1])
            slot3.setImageResource(imagesArray[2])
            slot4.setImageResource(imagesArray[3])
            slot5.setImageResource(imagesArray[4])
        }
    }
}