package com.eco4ndly.tracorona.features.main.di

import androidx.lifecycle.ViewModelProvider
import com.eco4ndly.tracorona.data.repo.CoronaDataRepositoryImpl
import com.eco4ndly.tracorona.features.main.CommonViewModel
import com.eco4ndly.tracorona.features.main.repo.CoronaDataRepository
import com.eco4ndly.tracorona.infra.di.factories.ViewModelFactory
import com.eco4ndly.tracorona.infra.di.scope.PerActivity
import com.eco4ndly.tracorona.infra.webservice.WebApi
import dagger.Module
import dagger.Provides

/**
 * A Sayan Porya code on 15/03/20
 */

@Module
class MainActivityModule {

  @PerActivity
  @Provides
  fun provideDataRepository(webApi: WebApi): CoronaDataRepository {
    return CoronaDataRepositoryImpl(webApi)
  }

  @Provides
  @PerActivity
  fun provideMainActivityViewModelFactory(commonVM: CommonViewModel): ViewModelProvider.Factory {
    return ViewModelFactory(commonVM)
  }
}