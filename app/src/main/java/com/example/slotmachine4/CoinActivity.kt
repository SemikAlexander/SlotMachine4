package com.example.slotmachine4

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.appcompat.app.AppCompatActivity
import com.example.slotmachine4.databinding.ActivityCoinBinding
import com.example.slotmachine4.game.Gifts
import com.example.slotmachine4.preferences.PrefsKeys
import com.example.slotmachine4.game.Sounds
import com.example.slotmachine4.preferences.PrefsKeysPrizes
import com.example.slotmachine4.view.startActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CoinActivity : AppCompatActivity() {
    lateinit var binding: ActivityCoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            everydayGiftTextView.setFactory(ViewSwitcher.ViewFactory {
                val textView = TextView(this@CoinActivity)
                textView.setTextColor(Color.BLACK)
                textView.textSize = 60F
                textView.gravity = Gravity.CENTER_HORIZONTAL
                textView
            })
        }
    }

    override fun onStart() {
        super.onStart()

        binding.apply {
            val sounds = Sounds()
            prizeButton.setOnClickListener {
                everydayGiftTextView.visibility = View.VISIBLE
                everydayGiftTextView.setText("+" + PrefsKeysPrizes.EVERYDAY_GOLD_GIFT.toString())

                sounds.playSound(R.raw.gift_coins, this@CoinActivity)

                val oa1 = ObjectAnimator.ofFloat(prizeButton, "scaleX", 1f, 0f)
                val oa2 = ObjectAnimator.ofFloat(prizeButton, "scaleX", 0f, 1f)
                oa1.interpolator = DecelerateInterpolator()
                oa2.interpolator = AccelerateDecelerateInterpolator()
                oa1.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        prizeButton.setImageResource(R.drawable.icon8)
                        oa2.start()
                    }
                })
                oa1.start()

                val gift = Gifts(getSharedPreferences(PrefsKeys.SETTING, Context.MODE_PRIVATE))

                if (gift.bigGift() != emptyList<Int>()) {
                    GlobalScope.launch {
                        delay(1000)
                        startActivity<BonusActivity>()
                        finish()
                    }
                }
                else {
                    GlobalScope.launch {
                        delay(1000)
                        startActivity<MainActivity>()
                        finish()
                    }
                }
            }
        }
    }
}