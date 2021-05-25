package com.example.slotmachine4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.marginTop
import com.example.slotmachine4.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        binding.apply {

        }
    }
}