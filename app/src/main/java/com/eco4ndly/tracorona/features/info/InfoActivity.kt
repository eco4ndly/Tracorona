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
    /*val str = getString(R.string.data_info)
    val strToClickable = "Novel Coronavirus (nCoV) Data Repository, provided by JHU CCSE"
    val startPoint = str.indexOf(strToClickable)
    val endPoint = startPoint + strToClickable.length
    val ss = SpannableString(str)
    val clickableSpan: ClickableSpan = object : ClickableSpan() {
      override fun onClick(p0: View) {
        Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/CSSEGISandData/COVID-19")).apply {
          startActivity(this)
        }
      }

      override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = true
      }
    }
    ss.setSpan(clickableSpan, startPoint, endPoint, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    binding.dataInfo.text = ss
    binding.dataInfo.movementMethod = LinkMovementMethod.getInstance()
    binding.dataInfo.highlightColor = Color.TRANSPARENT*/

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
