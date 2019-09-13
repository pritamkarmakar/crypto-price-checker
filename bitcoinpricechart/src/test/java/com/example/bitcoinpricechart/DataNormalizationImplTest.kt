package com.example.bitcoinpricechart

import com.example.model.MarketPrice
import com.example.model.Value
import com.github.mikephil.charting.data.LineDataSet
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class DataNormalizationImplTest {
    private lateinit var dataNormalizationImpl: DataNormalizationImpl
    private val value1 = Value(1.1F, 2.2F)
    private val value2 = Value(3.1F, 4.2F)
    private val testMarketPrice = MarketPrice("desc", "name", "period", "status", "unit", mutableListOf(value1, value2))

    @Before
    fun setUp() {
        dataNormalizationImpl = DataNormalizationImpl()
    }

    @Test
    fun normalizeData() {
        dataNormalizationImpl.normalizeData(testMarketPrice)
        assertEquals(1.1F, dataNormalizationImpl.getDataToBind().get()?.xMin)
        assertEquals(3.1F, dataNormalizationImpl.getDataToBind().get()?.xMax)
        assertEquals(2.2F, dataNormalizationImpl.getDataToBind().get()?.yMin)
        assertEquals(4.2F, dataNormalizationImpl.getDataToBind().get()?.yMax)
        assertEquals(2, (dataNormalizationImpl.getDataToBind().get()?.dataSets?.get(0) as LineDataSet).values.size)
    }
}