package com.eco4ndly.tracorona.utils.extensions

import okhttp3.Call
import okhttp3.Request
import retrofit2.Retrofit

/**
 * A Sayan Porya code on 2020-02-08
 */

@PublishedApi
internal inline fun Retrofit.Builder.callFactory(crossinline body: (Request) -> Call) =
  callFactory(object : Call.Factory {
    override fun newCall(request: Request): Call = body(request)
  })
