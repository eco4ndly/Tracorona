package com.eco4ndly.tracorona.features.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eco4ndly.tracorona.features.main.MainActivity

class SplashActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    Intent(this, MainActivity::class.java).apply {
      startActivity(this)
      finish()
    }
  }
}
