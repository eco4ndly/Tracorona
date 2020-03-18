package com.eco4ndly.tracorona.utils.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.lang.Exception

/**
 * A Sayan Porya code on 15/03/20
 */

val <T> T.exhaustive: T
  get() = this

fun String.launchBrowserIfUrl(context: Context) {
  try {
    Intent(Intent.ACTION_VIEW, Uri.parse(this)).apply {
      addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(this)
    }
  } catch (e: Exception) {

  }
}