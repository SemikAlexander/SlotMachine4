package com.example.slotmachine4

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import android.widget.ViewSwitcher
import com.example.slotmachine4.databinding.ActivityBonusBinding
import com.example.slotmachine4.game.Gifts
import com.example.slotmachine4.game.PrefsKeys
import com.example.slotmachine4.game.Sounds

class BonusActivity : AppCompatActivity() {
    lateinit var binding: ActivityBonusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBonusBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.apply {
            bigGift.setFactory(ViewSwitcher.ViewFactory {
                val textView = TextView(this@BonusActivity)
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
            val prize = listOf(prize1, prize2, prize3, prize4)
            val sounds = Sounds()

            val pref = getSharedPreferences(PrefsKeys.SETTING, Context.MODE_PRIVATE)
            val editor = pref.edit()

            val gift = Gifts(pref)
            val prizeValues = gift.bigGift()

            prize.indices.forEach{ i ->
                prize[i].setOnClickListener {
                    bigGift.visibility = View.VISIBLE
                    bigGift.setText("+" + prizeValues[i].toString())

                    sounds.playSound(R.raw.gift_coins, this@BonusActivity)

                    editor.putInt(
                            PrefsKeys.GOLD,
                            prizeValues[i]
                    )

                    editor.apply()

                    val oa1 = ObjectAnimator.ofFloat(prize[i], "scaleX", 1f, 0f)
                    val oa2 = ObjectAnimator.ofFloat(prize[i], "scaleX", 0f, 1f)
                    oa1.interpolator = DecelerateInterpolator()
                    oa2.interpolator = AccelerateDecelerateInterpolator()
                    oa1.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            prize[i].setImageResource(R.drawable.icon8)
                            oa2.start()
                        }
                    })
                    oa1.start()
                }
            }
        }
    }
}