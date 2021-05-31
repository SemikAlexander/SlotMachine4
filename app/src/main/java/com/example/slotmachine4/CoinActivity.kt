package com.example.slotmachine4

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.slotmachine4.databinding.ActivityCoinBinding
import com.example.slotmachine4.game.Game
import com.example.slotmachine4.game.PrefsKeys
import com.example.slotmachine4.view.startActivity

class CoinActivity : AppCompatActivity() {
    lateinit var binding: ActivityCoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = getSharedPreferences(PrefsKeys.SETTING, Context.MODE_PRIVATE)
        val gameProcess = Game(pref)

        gameProcess.makeGiftForUser()

        startActivity<MainActivity>()
    }
}