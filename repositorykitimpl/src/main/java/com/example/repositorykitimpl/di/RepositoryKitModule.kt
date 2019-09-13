package com.example.repositorykitimpl.di

import com.example.basekit.SchedulerProvider
import com.example.local.dao.PriceDao
import com.example.remote.BitcoinPriceSource
import com.example.repokit.RepositoryKit
import com.example.repositorykitimpl.RepositoryKitImpl
import dagger.Module
import dagger.Provides

@Module
class RepositoryKitModule {
    @Provides
    fun providesBitcoinPriceRepository(
        bitcoinPriceSource: BitcoinPriceSource,
        priceDao: PriceDao,
        schedulerProvider: SchedulerProvider
    ): RepositoryKit = RepositoryKitImpl(bitcoinPriceSource, priceDao, schedulerProvider)
}