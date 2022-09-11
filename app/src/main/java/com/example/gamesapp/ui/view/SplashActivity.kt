package com.example.gamesapp.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.example.gamesapp.R
import com.example.gamesapp.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide the toolbar
        supportActionBar?.hide()

        // Add animation on the splash screen
        setAnimation()

        // Coroutine to start MainActivity and finish SplashActivity
        CoroutineScope(Dispatchers.Main).launch {

            // delay of two seconds before going to next screen
            delay(2000)

            startActivity(Intent(this@SplashActivity, MainActivity::class.java))

            finish()
        }
    }

    private fun setAnimation() {
        val animationZoom = AnimationUtils.loadAnimation(this, R.anim.zoom_in)

        binding.ivTitle.startAnimation(animationZoom)
        binding.ivCharacters.startAnimation(animationZoom)
        binding.ivLogo.startAnimation(animationZoom)
    }
}