package com.example.slotmachine4

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.slotmachine4.databinding.ActivityMainBinding
import com.example.slotmachine4.game.Game
import com.example.slotmachine4.game.PrefsKeys
import com.example.slotmachine4.game.Slots
import com.example.slotmachine4.view.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random.Default.nextLong
import com.example.slotmachine4.R.drawable.*
import android.media.MediaPlayer
import android.view.MotionEvent


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var mMediaPlayer: MediaPlayer? = null

    private var from: Long = 0
    private var until: Long = 100

    private var pressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()

        val pref = getSharedPreferences(PrefsKeys.SETTING, Context.MODE_PRIVATE)
        val gameProcess = Game(pref)

        val slotsImages = setImagesInSlots()

        binding.apply {
            userGoldTextView.text = gameProcess.getUserCash().toString()
            userRate.text = gameProcess.rate.toString()

            increaseRate.setOnClickListener {

                try { insertCoin() } catch (e: Exception) {}

                gameProcess.userAction(PrefsKeys.INCREASE)
                setUserRateAndGold(gameProcess)
            }

            reduceRate.setOnClickListener {
                gameProcess.userAction(PrefsKeys.REDUCE)
                setUserRateAndGold(gameProcess)
            }

            spin.setOnClickListener {
                spinResultWindow.visibility = View.GONE

                playSound(R.raw.insert)

                if (gameProcess.rate > 0) {
                    val slotsImagesSpin = arrayListOf<Slots>()
                    val slotsImagesView = listOf(slot1, slot2, slot3, slot4, slot5)

                    for (i in 0 until 5) {
                        slotsImagesSpin.add(doSpin(slotsImagesView[i], slotsImages[i]))
                        from += 150
                        until += 200
                    }

                    from = 0
                    until = 100

                    GlobalScope.launch {
                        delay(6000)
                        launch(Dispatchers.Main) {
                            for (element in slotsImagesSpin)
                                element.stopSpin()

                            GlobalScope.launch {
                                delay(60)
                                launch(Dispatchers.Main) {
                                    val stopSlotsImages = arrayListOf<Int>()

                                    for (i in 0 until 5) {
                                        val id = slotsImagesSpin[i].idImage

                                        stopSlotsImages.add(id)
                                        slotsImagesView[i].setImageResource(id)
                                    }

                                    GlobalScope.launch {
                                        delay(60)
                                        launch(Dispatchers.Main) {
                                            showResult(gameProcess.spinResults(stopSlotsImages))
                                            setUserRateAndGold(gameProcess)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else
                    toast("Enter the rate!")
            }

            spin.setOnLongClickListener {
                spinResultWindow.visibility = View.GONE

                playSound(R.raw.insert)

                if (gameProcess.rate > 0) {
                    val slotsImagesSpin = arrayListOf<Slots>()
                    val slotsImagesView = listOf(slot5, slot4, slot3, slot2, slot1)

                    for (i in 0 until 5) {
                        slotsImagesSpin.add(doSpin(slotsImagesView[i], slotsImages[i]))
                        from += 150
                        until += 200
                    }

                    from = 0
                    until = 100

                    GlobalScope.launch {
                        delay(4000)
                        launch(Dispatchers.Main) {
                            for (element in slotsImagesSpin)
                                element.stopSpin()

                            GlobalScope.launch {
                                delay(60)
                                launch(Dispatchers.Main) {
                                    val stopSlotsImages = arrayListOf<Int>()

                                    for (i in 0 until 5) {
                                        val id = slotsImagesSpin[i].idImage

                                        stopSlotsImages.add(id)
                                        slotsImagesView[i].setImageResource(id)
                                    }

                                    GlobalScope.launch {
                                        delay(60)
                                        launch(Dispatchers.Main) {
                                            showResult(gameProcess.spinResults(stopSlotsImages))
                                            setUserRateAndGold(gameProcess)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                else
                    toast("Enter the rate!")

                return@setOnLongClickListener true
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

    private fun setImagesInSlots(): ArrayList<MutableList<Int>> {
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

    private fun setUserRateAndGold(gameProcess: Game) {
        binding.apply {
            userRate.text = gameProcess.rate.toString()
            userGoldTextView.text = gameProcess.userGold.toString()
        }
    }

    private fun doSpin(
            slot: ImageView,
            imagesArray: MutableList<Int>
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

    private fun insertCoin() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(this, R.raw.coin)
            mMediaPlayer!!.start()
        } else mMediaPlayer!!.start()
    }

    private fun playSound(musicID: Int) {
        mMediaPlayer = MediaPlayer.create(this, musicID)
        mMediaPlayer!!.start()

        GlobalScope.launch {
            delay(800)
            launch(Dispatchers.Main) {
                mMediaPlayer!!.stop()
                mMediaPlayer!!.release()
                mMediaPlayer = null
            }
        }
    }

    private fun showResult(result: String) {
        binding.apply {
            spinResultWindow.visibility = View.VISIBLE

            if (result == PrefsKeys.NO_PRIZE) {
                trophy.setImageResource(ic_lose)
                playSound(R.raw.lose)
            }
            else {
                trophy.setImageResource(ic_trophy)
                playSound(R.raw.coins_collect)
            }

            resultTextView.text = result

            okButton.setOnClickListener {
                spinResultWindow.visibility = View.GONE
            }
        }
    }
}