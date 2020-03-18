package com.eco4ndly.tracorona.features.info

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.eco4ndly.tracorona.R
import com.eco4ndly.tracorona.databinding.ActivityInfoBinding
import com.eco4ndly.tracorona.utils.extensions.clicks
import com.eco4ndly.tracorona.utils.extensions.launchBrowserIfUrl
import com.eco4ndly.tracorona.utils.extensions.makeTextPortionClickable
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class InfoActivity : AppCompatActivity() {

  private lateinit var binding: ActivityInfoBinding
  private val mainScope = MainScope()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityInfoBinding.inflate(layoutInflater)
    setContentView(binding.root)

    binding.dataInfo.makeTextPortionClickable(
        body = getString(R.string.data_info),
        clickableText = "Novel Coronavirus (nCoV) Data Repository, provided by JHU CCSE"
    ) {
      Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/CSSEGISandData/COVID-19")).apply {
        startActivity(this)
      }
    }

    binding.tvMore
        .clicks()
        .onEach {
          "https://github.com/ExpDev07/coronavirus-tracker-api".launchBrowserIfUrl(this)
        }
        .launchIn(mainScope)
  }

  override fun onDestroy() {
    super.onDestroy()
    mainScope.cancel()
  }
}
