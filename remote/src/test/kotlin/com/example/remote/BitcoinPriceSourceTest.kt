package com.example.remote

import com.example.remote.api.BitcoinPriceApi
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

class BitcoinPriceSourceTest {
    private lateinit var bitcoinPriceSource: BitcoinPriceSource
    private val bitcoinPriceApi: BitcoinPriceApi = mock()
    private val testTimeSpan = "30days"

    @Before
    fun setUp() {
        bitcoinPriceSource = BitcoinPriceSourceImpl(bitcoinPriceApi)
    }

    @Test
    fun fetchBitcoinPrice() {
        bitcoinPriceSource.fetchBitcoinPrice(testTimeSpan)
        verify(bitcoinPriceApi, times(1)).fetchBitcoinPrice(testTimeSpan)
    }
}