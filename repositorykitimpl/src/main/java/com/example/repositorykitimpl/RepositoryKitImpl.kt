package com.example.repositorykitimpl

import com.example.basekit.SchedulerProvider
import com.example.local.dao.PriceDao
import com.example.local.model.MarketPriceRoom
import com.example.model.MarketPrice
import com.example.remote.BitcoinPriceSource
import com.example.repokit.RepositoryKit
import io.reactivex.*

internal class RepositoryKitImpl(
    private val bitcoinPriceSource: BitcoinPriceSource,
    private val priceDao: PriceDao,
    private val scheduler: SchedulerProvider
) : RepositoryKit {
    override fun getBitcoinPrice(timeSpan: String): Observable<MarketPrice> {
        return Observable.mergeDelayError(getDataFromDb(timeSpan), getDataFromApi(timeSpan))
    }

    private fun getDataFromApi(timeSpan: String): Observable<MarketPrice> {
        return bitcoinPriceSource.fetchBitcoinPrice(timeSpan).doOnNext {
            var marketPriceRoom = MarketPriceRoom(timeSpan, it)
            storeBitcoinPriceInDb(marketPriceRoom)
        }
    }

    private fun getDataFromDb(timeSpan: String): Observable<MarketPrice> {
        return priceDao.getBitcoinPrice(timeSpan)
            .flatMapObservable { Observable.just(it.marketPrice) }
    }

    private fun storeBitcoinPriceInDb(marketPriceRoom: MarketPriceRoom) {
        Observable.fromCallable { priceDao.insert(marketPriceRoom) }
            .subscribeOn(scheduler.ioSchedulerProvider())
            .subscribe()
    }
}