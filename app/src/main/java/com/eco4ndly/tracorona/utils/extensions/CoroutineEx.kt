package com.eco4ndly.tracorona.utils.extensions

import android.view.View
import android.widget.EditText
import com.eco4ndly.tracorona.data.api.ApiResult
import com.eco4ndly.tracorona.utils.Utils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.io.IOException

/**
 * A Sayan Porya code on 2020-02-08
 */

@ExperimentalCoroutinesApi
fun <T : Any> Flow<ApiResult<T>>.applyCommonStuffs() =
  retryWhen { cause, attempt ->
    when {
      (cause is IOException && attempt < Utils.MAX_RETRIES) -> {
        delay(Utils.getBackoffDelay(attempt))
        true
      }
      else -> {
        false
      }
    }
  }
      .onStart { emit(ApiResult.Loading(isLoading = true)) }
      .onCompletion { emit(ApiResult.Loading(isLoading = false)) }

fun Job?.cancelIfActive() {
  if (this?.isActive == true) {
    cancel()
  }
}

@ExperimentalCoroutinesApi
fun View.clicks(): Flow<Unit> = callbackFlow {
  val listener = View.OnClickListener { safeOffer(Unit) }
  setOnClickListener(listener)
  awaitClose {
    setOnClickListener(null)
  }
}

@ExperimentalCoroutinesApi
fun <E> SendChannel<E>.safeOffer(value: E) = !isClosedForSend && try {
  offer(value)
} catch (t: Throwable) {
  // Ignore all
  false
}