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

        binding.apply {
            usersGiftTextView.setFactory(ViewSwitcher.ViewFactory {
                val textView = TextView(this@CoinActivity)
                textView.setTextColor(BLACK)
                textView.textSize = 60F
                textView.gravity = CENTER_HORIZONTAL
                textView
            })
        }
    }

    override fun onStart() {
        super.onStart()

        val pref = getSharedPreferences(PrefsKeys.SETTING, Context.MODE_PRIVATE)
        val gameProcess = Game(pref)

        binding.apply {
            YoYo.with(Techniques.Tada)
                    .duration(500)
                    .repeat(5)
                    .playOn(giftButton)

            giftButton.setOnClickListener{
                giftButton.setImageResource(ic_open_gift)

                usersGiftTextView.visibility = View.VISIBLE
                usersGiftTextView.setText("+50")

                gameProcess.makeGiftForUser()
            }
        }
    }
}