package com.example.slotmachine4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.graphics.Color.BLACK
import android.media.MediaPlayer
import android.view.Gravity.CENTER_HORIZONTAL
import android.view.View
import android.widget.TextView
import android.widget.ViewSwitcher
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.slotmachine4.R.drawable.*
import com.example.slotmachine4.databinding.ActivityHelpGiftBinding
import com.example.slotmachine4.game.Gifts
import com.example.slotmachine4.preferences.PrefsKeys
import com.example.slotmachine4.game.Sounds
import com.example.slotmachine4.preferences.PrefsKeysPrizes
import com.example.slotmachine4.view.startActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HelpGiftActivity : AppCompatActivity() {
    lateinit var binding: ActivityHelpGiftBinding
    private var mMediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHelpGiftBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            usersGiftTextView.setFactory(ViewSwitcher.ViewFactory {
                val textView = TextView(this@HelpGiftActivity)
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
        val gift = Gifts(pref)

        binding.apply {
            YoYo.with(Techniques.Tada)
                .duration(500)
                .repeat(5)
                .playOn(giftButton)

            giftButton.setOnClickListener{
                giftButton.setImageResource(icon9)
                val sounds = Sounds()

                usersGiftTextView.visibility = View.VISIBLE
                usersGiftTextView.setText("+${PrefsKeysPrizes.GOLD_GIFT}")

                gift.makeSaveGiftForUser()

                sounds.playSound(R.raw.gift_coins, this@HelpGiftActivity)

                GlobalScope.launch {
                    delay(800)
                    startActivity<MainActivity>()
                    finish()
                }
            }
        }
    }
}