package com.example.blockchain.di

import android.app.Application
import android.content.Context
import com.example.basekit.SchedulerProvider
import com.example.basekit.ToastMaker
import com.example.basekitimpl.SchedulerProviderImpl
import com.example.basekitimpl.ToastMakerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {

    @JvmStatic
    @Singleton
    @Provides
    fun providesSchedulerProvider(): SchedulerProvider = SchedulerProviderImpl()

    @JvmStatic
    @Singleton
    @Provides
    fun toastMaker(context: Context): ToastMaker = ToastMakerImpl(context)

    @JvmStatic
    @Provides
    fun context(app: Application): Context = app
}