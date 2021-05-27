package com.munawirfikri.saveoury.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.munawirfikri.saveoury.R
import com.munawirfikri.saveoury.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var handler : Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handler = Handler(mainLooper)
        handler.postDelayed({
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 2500)
    }
}