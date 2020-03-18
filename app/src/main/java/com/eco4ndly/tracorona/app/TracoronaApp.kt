package com.eco4ndly.tracorona.app

import android.app.Application
import com.eco4ndly.tracorona.infra.di.DaggerAppComponent
import com.google.gson.Gson
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import javax.inject.Inject

/**
 * A Sayan Porya code on 15/03/20
 */
class TracoronaApp : Application(), HasAndroidInjector {
  @Inject
  lateinit var androidInjector: DispatchingAndroidInjector<Any>

  @Inject
  lateinit var _gson: Gson

  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    DaggerAppComponent
        .builder()
        .application(this)
        .build()
        .inject(this)

    gson = _gson
  }

  companion object {
    lateinit var gson: Gson
  }

  override fun androidInjector(): AndroidInjector<Any> = androidInjector

}