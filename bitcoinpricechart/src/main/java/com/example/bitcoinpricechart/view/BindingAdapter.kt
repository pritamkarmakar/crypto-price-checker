package com.example.bitcoinpricechart.view

import androidx.databinding.BindingAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData

@BindingAdapter("app:addSeries")
fun addSeries(lineChart: LineChart, lineData: LineData?) {
    lineChart.data = lineData
    lineChart.invalidate()
}