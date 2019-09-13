package com.example.bitcoinpricechart.di.component

import com.example.basekit.SchedulerProvider
import com.example.basekit.ToastMaker
import com.example.bitcoinpricechart.di.module.BitcoinPriceModule
import com.example.bitcoinpricechart.view.BitcoinPriceActivity
import com.example.repokit.RepositoryKit
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [BitcoinPriceModule::class], dependencies = [FeatureDependencies::class])
interface BitcoinPriceComponent {
    @Component.Builder
    interface Builder {
        fun build(): BitcoinPriceComponent
        fun dependencies(dependencies: FeatureDependencies): Builder
    }

    fun inject(activity: BitcoinPriceActivity)
}

interface FeatureDependencies {
    fun repositoryKit(): RepositoryKit
    fun scheduleProvider(): SchedulerProvider
    fun toastMaker(): ToastMaker
}

interface FeatureDependenciesProvider {
    fun provideFeatureDependencies(): FeatureDependencies
}