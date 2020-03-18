package com.eco4ndly.tracorona.infra.di

import android.app.Application
import com.eco4ndly.tracorona.app.TracoronaApp
import com.eco4ndly.tracorona.infra.di.modules.ActivityBuilder
import com.eco4ndly.tracorona.infra.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * A Sayan Porya code on 2020-02-09
 */

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityBuilder::class
    ]
)
interface AppComponent {
    fun inject(app: TracoronaApp)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}