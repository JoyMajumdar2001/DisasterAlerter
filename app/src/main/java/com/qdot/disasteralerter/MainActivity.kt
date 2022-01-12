package com.qdot.disasteralerter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.qdot.disasteralerter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.apply {
            adminButton.setOnClickListener {
                startActivity(Intent(this@MainActivity,AdminActivity::class.java))
            }
            userButton.setOnClickListener {
                startActivity(Intent(this@MainActivity,UserActivity::class.java))
            }
        }
    }
}