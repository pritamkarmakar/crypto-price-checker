package com.example.blockchain.di

import android.content.Context
import com.example.basekit.SchedulerProvider
import com.example.basekit.ToastMaker
import com.example.basekitimpl.SchedulerProviderImpl
import com.example.basekitimpl.ToastMakerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {
    @Singleton
    @Provides
    fun providesSchedulerProvider(): SchedulerProvider = SchedulerProviderImpl()

    @Singleton
    @Provides
    fun toastMaker(): ToastMaker = ToastMakerImpl(context)
}