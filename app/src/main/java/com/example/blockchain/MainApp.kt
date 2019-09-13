package com.example.blockchain

import android.app.Application
import com.example.bitcoinpricechart.di.component.FeatureDependencies
import com.example.bitcoinpricechart.di.component.FeatureDependenciesProvider
import com.example.blockchain.di.AppComponent
import com.example.blockchain.di.AppModule
import com.example.blockchain.di.DaggerAppComponent

class MainApp : Application(), FeatureDependenciesProvider {
    private lateinit var appComponent: AppComponent
    private lateinit var rootDependenciesProvider: RootDependenciesProvider
    override fun onCreate() {
        appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .bindApp(this)
            .build()
        super.onCreate()
        rootDependenciesProvider = RootDependenciesProvider(this)
    }

    fun getAppComponent(): AppComponent {
        return appComponent
    }

    override fun provideFeatureDependencies(): FeatureDependencies {
        return rootDependenciesProvider.provideFeatureDependencies()
    }
}