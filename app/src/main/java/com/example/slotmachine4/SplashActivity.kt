package com.example.slotmachine4

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.slotmachine4.databinding.ActivitySplashBinding
import com.example.slotmachine4.game.Game
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
        val gameProcess = Game(pref)

        if (gameProcess.getUserCash() <= 1)
            startActivity<CoinActivity>()
        else {
            GlobalScope.launch {
                delay(2000)
                startActivity<MainActivity>()
                finish()
            }
        }
    }
}