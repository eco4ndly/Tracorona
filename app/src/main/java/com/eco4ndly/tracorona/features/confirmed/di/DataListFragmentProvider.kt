package com.eco4ndly.tracorona.features.confirmed.di

import com.eco4ndly.tracorona.features.confirmed.ui.DataListFragment
import com.eco4ndly.tracorona.infra.di.scope.PerFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * A Sayan Porya code on 15/03/20
 */

@Module
abstract class DataListFragmentProvider {

    @ContributesAndroidInjector(modules = [DataListFragmentModule::class])
    @PerFragment
    abstract fun provideDataListFragment(): DataListFragment
}