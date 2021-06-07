package com.example.slotmachine4

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.slotmachine4.databinding.ActivitySplashBinding
import com.example.slotmachine4.game.Game
import com.example.slotmachine4.game.Gifts
import com.example.slotmachine4.game.PrefsKeys
import com.example.slotmachine4.view.startActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = getSharedPreferences(PrefsKeys.SETTING, Context.MODE_PRIVATE)
        val gift = Gifts(pref)

        GlobalScope.launch {
            delay(2000)
            when {
                pref.getInt(PrefsKeys.GOLD, 250) <= 1 -> {
                    startActivity<HelpGiftActivity>()
                    finish()
                }
                gift.everydayGift() == 10 -> {
                    startActivity<CoinActivity>()
                    finish()
                }
                else -> {
                    startActivity<MainActivity>()
                    finish()
                }
            }
        }
    }
}