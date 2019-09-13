package com.example.bitcoinpricechart.view

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class DateAxisFormatter : IAxisValueFormatter {
    override fun getFormattedValue(
        value: Float,
        axis: AxisBase
    ): String {
        val date = Date((value * 1000).toLong())
        val sdf = SimpleDateFormat("MMM-yy", Locale.ENGLISH)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(date)
    }
}