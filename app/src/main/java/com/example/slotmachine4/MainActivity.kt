package com.example.slotmachine4

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.slotmachine4.R.drawable.ic_money
import com.example.slotmachine4.R.drawable.ic_sad
import com.example.slotmachine4.databinding.ActivityMainBinding
import com.example.slotmachine4.game.*
import com.example.slotmachine4.preferences.PrefsKeys
import com.example.slotmachine4.preferences.PrefsKeysActions
import com.example.slotmachine4.preferences.PrefsKeysPrizes
import com.example.slotmachine4.view.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.slotmachine4.view.startActivity
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var from: Long = 0
    private var until: Long = 100

    private var pressedTime: Long = 0

    private val gameProcess = Game()
    private val gameResult = Results()
    private val sounds = Sounds()
    private val statistics = Statistics()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        val pref = getSharedPreferences(PrefsKeys.SETTING, Context.MODE_PRIVATE)

        statistics.setValues(pref)

        val slotsImages = gameProcess.setImagesInSlots()

        binding.apply {
            gameProcess.userGold = pref.getInt(PrefsKeys.GOLD, 250)
            userGoldTextView.text = gameProcess.userGold.toString()
            userRate.text = gameProcess.rate.toString()

            val slotsImagesView = listOf(slot1, slot2, slot3, slot4, slot5)

            userGoldTextView.setOnLongClickListener {
                startActivity<StatisticActivity>()
                return@setOnLongClickListener true
            }

            increaseRate.setOnClickListener {

                try { sounds.insertCoin(this@MainActivity) } catch (e: Exception) {}

                gameProcess.userAction(PrefsKeysActions.INCREASE)
                setUserRateAndGold(gameProcess)
            }

            reduceRate.setOnClickListener {
                gameProcess.userAction(PrefsKeysActions.REDUCE)
                setUserRateAndGold(gameProcess)
            }

            spin.setOnClickListener {
                spinResultWindow.visibility = View.GONE

                sounds.playSoundInGame(R.raw.insert, this@MainActivity)

                statistics.spinCount++

                if (gameProcess.rate > 0) {
                    val slotsImagesSpin = arrayListOf<Slots>()

                    for (i in 0 until 5) {
                        slotsImagesSpin.add(
                                gameProcess.startSpin(
                                        slotsImagesView[i],
                                        slotsImages[i],
                                        from,
                                        until,
                                        this@MainActivity
                                )
                        )
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
                                            showResult(
                                                    gameResult.spinResults(
                                                            stopSlotsImages,
                                                            gameProcess,
                                                            pref,
                                                            statistics
                                                    ),
                                                    statistics,
                                                    pref
                                            )
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

                sounds.playSoundInGame(R.raw.insert, this@MainActivity)

                statistics.spinCount++

                if (gameProcess.rate > 0) {
                    val slotsImagesSpin = arrayListOf<Slots>()
                    val slotsImagesView = listOf(slot5, slot4, slot3, slot2, slot1)

                    for (i in 0 until 5) {
                        slotsImagesSpin.add(
                                gameProcess.startSpin(
                                        slotsImagesView[i],
                                        slotsImages[i],
                                        from,
                                        until,
                                        this@MainActivity
                                )
                        )
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
                                            showResult(
                                                    gameResult.spinResults(
                                                            stopSlotsImages,
                                                            gameProcess,
                                                            pref,
                                                            statistics
                                                    ),
                                                    statistics,
                                                    pref
                                            )
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

            slotsImagesView.indices.forEach { i ->
                slotsImagesView[i].setOnLongClickListener {
                    val images = slotsImages[Random.nextInt(0, 4)]

                    sounds.playSound(R.raw.change, this@MainActivity)

                    slotsImagesView[i].setImageResource(images[Random.nextInt(0, 4)])
                    return@setOnLongClickListener true
                }

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

    private fun showResult(result: String, statistics: Statistics, preferences: SharedPreferences) {
        binding.apply {
            spinResultWindow.visibility = View.VISIBLE
            val sounds = Sounds()

            if (result == PrefsKeysPrizes.NO_PRIZE) {
                trophy.setImageResource(ic_sad)
                sounds.playSoundInGame(R.raw.lose, this@MainActivity)
            }
            else {
                trophy.setImageResource(ic_money)
                sounds.playSoundInGame(R.raw.coins_collect, this@MainActivity)
            }

            resultTextView.text = result

            okButton.setOnClickListener {
                spinResultWindow.visibility = View.GONE

                statistics.saveStatInfo(preferences)

                val message = statistics.thanksMessage()
                if (message != "")
                    toast(message)
            }
        }
    }
}