package com.eco4ndly.tracorona.infra.di.modules

import com.eco4ndly.tracorona.features.confirmed.di.DataListFragmentProvider
import com.eco4ndly.tracorona.features.main.MainActivity
import com.eco4ndly.tracorona.features.main.di.MainActivityModule
import com.eco4ndly.tracorona.infra.di.scope.PerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * A Sayan Porya code on 14/03/20
 */

@Module
abstract class ActivityBuilder {
  @PerActivity
  @ContributesAndroidInjector(
      modules = [
        MainActivityModule::class,
        DataListFragmentProvider::class
      ]
  )
  abstract fun bindPdfListActivity(): MainActivity
}