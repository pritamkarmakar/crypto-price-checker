package com.example.repokit

import com.example.model.MarketPrice
import io.reactivex.Observable

interface RepositoryKit {
    fun getBitcoinPrice(timeSpan:String): Observable<MarketPrice>
}