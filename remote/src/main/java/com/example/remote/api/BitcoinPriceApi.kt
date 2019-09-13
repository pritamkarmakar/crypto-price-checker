package com.example.remote.api

import com.example.model.MarketPrice
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface BitcoinPriceApi {
    @GET("/charts/market-price?cors=true&format=json&lang=en")
    fun fetchBitcoinPrice(@Query("timespan") timeSpan:String): Observable<MarketPrice>
}