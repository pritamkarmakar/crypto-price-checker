package com.example.bitcoinpricechart.di.module

import com.example.basekit.SchedulerProvider
import com.example.basekit.ToastMaker
import com.example.bitcoinpricechart.DataNormalization
import com.example.bitcoinpricechart.DataNormalizationImpl
import com.example.bitcoinpricechart.viewmodel.*
import com.example.repokit.RepositoryKit
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
object BitcoinPriceModule {

    @JvmStatic
    @Provides
    fun providesViewModel(
        repositoryKit: RepositoryKit,
        toastMaker: ToastMaker,
        dataNormalization: DataNormalization,
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable
    ): BitcoinPriceViewModel =
        BitcoinPriceViewModel(repositoryKit, toastMaker, dataNormalization, schedulerProvider, compositeDisposable)

    @JvmStatic
    @Provides
    @Singleton
    fun providesDataNormalization(): DataNormalization =
        DataNormalizationImpl()

    @JvmStatic
    @Provides
    @Singleton
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()
}