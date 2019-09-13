package com.example.bitcoinpricechart

import androidx.databinding.ObservableField
import com.example.model.MarketPrice
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import io.reactivex.Completable

/**
 * Contract to normalize data that can be bind to the chart
 */
interface DataNormalization {
    fun normalizeData(marketPrice: MarketPrice): Completable
    fun getDataToBind(): ObservableField<LineData>
}

/**
 * data normalization as need by MPAndroidChart (https://github.com/PhilJay/MPAndroidChart)
 */
class DataNormalizationImpl : DataNormalization {
    private var dataToBind = ObservableField<LineData>()

    override fun normalizeData(marketPrice: MarketPrice): Completable {
        val entries = ArrayList<Entry>()
        for (entry in marketPrice.values) {
            entries.add(Entry(entry.x, entry.y))
        }
        val dataSet = LineDataSet(entries, "Bitcoin Price Chart")
        val lineData = LineData(dataSet)
        dataToBind.set(lineData)
        return Completable.complete()
    }

    override fun getDataToBind(): ObservableField<LineData> {
        return dataToBind
    }
}
