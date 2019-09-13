package com.example.local.di

import android.app.Application
import com.example.local.dao.PriceDao
import com.example.local.database.BlockchainDatabase
import dagger.Module
import dagger.Provides

@Module
class PriceDaoModule {
    @Provides
    fun providesPriceDaoModule(application: Application): PriceDao = BlockchainDatabase.buildDatabase(application).priceDao()
}