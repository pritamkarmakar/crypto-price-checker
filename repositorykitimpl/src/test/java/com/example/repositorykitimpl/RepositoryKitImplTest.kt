package com.example.repositorykitimpl

import com.example.basekit.SchedulerProvider
import com.example.local.dao.PriceDao
import com.example.local.model.MarketPriceRoom
import com.example.model.MarketPrice
import com.example.remote.BitcoinPriceSource
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RepositoryKitImplTest {
    private lateinit var repositoryKitImpl: RepositoryKitImpl
    private val bitcoinPriceSource: BitcoinPriceSource = mock()
    private val priceDao: PriceDao = mock()
    private val schedulerProvider: SchedulerProvider = TestSchedulerProvider()
    private val marketPriceFromDatabase = MarketPrice("market price from db", "name", "period", "status", "unit", mutableListOf())
    private val marketPriceFromApi = MarketPrice("market price from api", "name", "period", "status", "unit", mutableListOf())
    private val timeSpan = "30days"
    private val marketPriceRoom = MarketPriceRoom(timeSpan, marketPriceFromDatabase)

    @Before
    fun setUp() {
        repositoryKitImpl = RepositoryKitImpl(bitcoinPriceSource, priceDao, schedulerProvider)
    }

    @Test
    fun `verify getBitcoinPrice when both db and api both returning data`() {
        whenever(priceDao.getBitcoinPrice(any())).thenReturn(Maybe.just(marketPriceRoom))
        whenever(bitcoinPriceSource.fetchBitcoinPrice(any())).thenReturn(Observable.just(marketPriceFromApi))
        val testObserver = repositoryKitImpl.getBitcoinPrice(timeSpan).test()

        // verify all invocations are happening
        verify(bitcoinPriceSource, times(1)).fetchBitcoinPrice(timeSpan)
        verify(priceDao, times(1)).getBitcoinPrice(timeSpan)
        verify(priceDao, times(1)).insert(MarketPriceRoom(timeSpan, marketPriceFromApi))
        // verify observable values include both the data coming from db and api
        Assert.assertTrue(testObserver.values().contains(marketPriceFromDatabase))
        Assert.assertTrue(testObserver.values().contains(marketPriceFromApi))
        testObserver.dispose()
    }

    @Test
    fun `verify getBitcoinPrice when db is empty`() {
        whenever(priceDao.getBitcoinPrice(any())).thenReturn(Maybe.empty())
        whenever(bitcoinPriceSource.fetchBitcoinPrice(any())).thenReturn(Observable.just(marketPriceFromApi))
        val testObserver = repositoryKitImpl.getBitcoinPrice(timeSpan).test()

        // verify all invocations are happening
        verify(bitcoinPriceSource, times(1)).fetchBitcoinPrice(timeSpan)
        verify(priceDao, times(1)).getBitcoinPrice(timeSpan)
        verify(priceDao, times(1)).insert(MarketPriceRoom(timeSpan, marketPriceFromApi))
        // verify observable values include only from api and not from the db
        Assert.assertFalse(testObserver.values().contains(marketPriceFromDatabase))
        Assert.assertTrue(testObserver.values().contains(marketPriceFromApi))
        testObserver.dispose()
    }

    @Test
    fun `verify getBitcoinPrice when api fails`() {
        whenever(priceDao.getBitcoinPrice(any())).thenReturn(Maybe.just(marketPriceRoom))
        whenever(bitcoinPriceSource.fetchBitcoinPrice(any())).thenReturn(Observable.error(Throwable()))
        val testObserver = repositoryKitImpl.getBitcoinPrice(timeSpan).test()

        // verify all invocations are happening
        verify(bitcoinPriceSource, times(1)).fetchBitcoinPrice(timeSpan)
        verify(priceDao, times(1)).getBitcoinPrice(timeSpan)
        verify(priceDao, never()).insert(any())
        // verify observable values include only from db and not from the api
        Assert.assertTrue(testObserver.values().contains(marketPriceFromDatabase))
        Assert.assertFalse(testObserver.values().contains(marketPriceFromApi))
        testObserver.dispose()
    }
}

class TestSchedulerProvider : SchedulerProvider {
    override fun ioSchedulerProvider(): Scheduler = Schedulers.trampoline()
    override fun uiSchedulerProvider(): Scheduler = Schedulers.trampoline()
}