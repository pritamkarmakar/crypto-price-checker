package com.example.blockchain.di

import android.app.Application
import com.example.basekit.SchedulerProvider
import com.example.basekit.ToastMaker
import com.example.local.di.PriceDaoModule
import com.example.remote.di.RemoteModule
import com.example.repokit.RepositoryKit
import com.example.repositorykitimpl.di.RepositoryKitModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, RepositoryKitModule::class, PriceDaoModule::class, RemoteModule::class])
@Singleton
interface AppComponent : AppDependencies {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): AppComponent
    }
}

interface AppDependencies {
    fun repokit(): RepositoryKit
    fun scheduleProvider(): SchedulerProvider
    fun toastMaker(): ToastMaker
}

