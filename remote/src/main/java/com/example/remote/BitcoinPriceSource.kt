package com.example.remote

import com.example.model.MarketPrice
import com.example.remote.api.BitcoinPriceApi
import io.reactivex.Observable

interface BitcoinPriceSource {
    fun fetchBitcoinPrice(timeSpan: String): Observable<MarketPrice>
}

class BitcoinPriceSourceImpl(private val bitcoinPriceApi: BitcoinPriceApi) : BitcoinPriceSource {
    override fun fetchBitcoinPrice(timeSpan: String): Observable<MarketPrice> {
        return bitcoinPriceApi.fetchBitcoinPrice(timeSpan)
    }
}