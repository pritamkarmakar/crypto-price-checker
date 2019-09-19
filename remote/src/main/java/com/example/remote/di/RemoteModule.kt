package com.example.remote.di

import com.example.remote.BitcoinPriceSource
import com.example.remote.BitcoinPriceSourceImpl
import com.example.remote.api.BitcoinPriceApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
object RemoteModule {
    @JvmStatic
    @Provides
    fun provideBitcoinPriceSource(bitcoinPriceApi: BitcoinPriceApi): BitcoinPriceSource = BitcoinPriceSourceImpl(bitcoinPriceApi)

    @JvmStatic
    @Provides
    @Singleton
    fun provideBitcoinPriceApi(retrofit: Retrofit): BitcoinPriceApi = retrofit.create(BitcoinPriceApi::class.java)

    @JvmStatic
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson?): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.blockchain.info")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideGson(): Gson {
        val builder = GsonBuilder()
        builder.setLenient()
        builder.registerTypeAdapter(Date::class.java, JsonDeserializer<Date> { json, _, _ ->
            if (json.asJsonPrimitive.isNumber) {
                Date(json.asJsonPrimitive.asLong * 1000)
            } else {
                null
            }
        })
        return builder.create()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @JvmStatic
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return interceptor
    }
}