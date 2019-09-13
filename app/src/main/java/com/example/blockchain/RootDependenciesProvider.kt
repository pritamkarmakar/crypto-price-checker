package com.example.blockchain

import com.example.basekit.SchedulerProvider
import com.example.basekit.ToastMaker
import com.example.bitcoinpricechart.di.component.FeatureDependencies
import com.example.bitcoinpricechart.di.component.FeatureDependenciesProvider
import com.example.blockchain.di.AppComponent
import com.example.repokit.RepositoryKit

/**
 * dependency provider for the entire app, any module will get the external dependencies through this class
 */
class RootDependenciesProvider(application: MainApp) : FeatureDependenciesProvider {
    var appComponent: AppComponent = application.getAppComponent()

    override fun provideFeatureDependencies(): FeatureDependencies = object : FeatureDependencies {
        override fun repositoryKit(): RepositoryKit = appComponent.repokit()
        override fun scheduleProvider(): SchedulerProvider = appComponent.scheduleProvider()
        override fun toastMaker(): ToastMaker = appComponent.toastMaker()
    }
}