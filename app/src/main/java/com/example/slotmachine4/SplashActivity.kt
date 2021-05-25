package com.example.slotmachine4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.slotmachine4.view.startActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        GlobalScope.launch {
            delay(2000)
            startActivity<MainActivity>()
            finish()
        }
    }
}