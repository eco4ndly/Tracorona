package com.eco4ndly.tracorona.data.api

import com.eco4ndly.tracorona.app.TracoronaApp
import okhttp3.ResponseBody

/**
 * A Sayan Porya code on 15/03/20
 */

object ErrorHandler {
  const val UNKNOWN_ERROR = "An unknown error occurred!"

  inline fun <reified T> parseError(responseBody: ResponseBody?): T? {

    responseBody?.string()?.run {
      try {
        return TracoronaApp.gson.fromJson(this, T::class.java)
      } catch (e: Exception) {
        e.printStackTrace()
      }
    }
    return null
  }
}