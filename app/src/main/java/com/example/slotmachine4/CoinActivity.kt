package com.example.slotmachine4

import android.content.Context
import android.graphics.Color.BLACK
import android.os.Bundle
import android.view.Gravity.CENTER_HORIZONTAL
import android.view.View
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.appcompat.app.AppCompatActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.slotmachine4.R.drawable.*
import com.example.slotmachine4.databinding.ActivityCoinBinding
import com.example.slotmachine4.game.Game
import com.example.slotmachine4.game.PrefsKeys


class CoinActivity : AppCompatActivity() {
    lateinit var binding: ActivityCoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
    }
}