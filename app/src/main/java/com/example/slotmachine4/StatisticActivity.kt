package com.example.slotmachine4

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.slotmachine4.databinding.ActivityStatisticBinding
import com.example.slotmachine4.game.Statistics
import com.example.slotmachine4.preferences.PrefsKeys
import com.example.slotmachine4.view.toast

class StatisticActivity : AppCompatActivity() {
    lateinit var binding: ActivityStatisticBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityStatisticBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val pref = getSharedPreferences(PrefsKeys.SETTING, Context.MODE_PRIVATE)
        val statistics = Statistics()

        binding.apply {
            val textViews = listOf(
                    userGold, x5Prize, x4Prize,
                    x3Prize, x2Prize, userSpins
            )

            val stat = statistics.getStatInfo(pref)

            for (i in 0 until stat.size) {
                textViews[i].text = stat[i].toString()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}