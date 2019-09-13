package com.example.bitcoinpricechart.viewmodel

import android.os.Bundle
import androidx.databinding.ObservableField
import com.example.model.MarketPrice
import com.example.repokit.RepositoryKit
import io.reactivex.rxkotlin.subscribeBy
import android.view.View
import androidx.databinding.ObservableInt
import com.example.basekit.SchedulerProvider
import com.example.basekit.ToastMaker
import com.example.bitcoinpricechart.DataNormalization
import com.example.bitcoinpricechart.R
import io.reactivex.disposables.CompositeDisposable
import java.io.IOException

const val BITCOIN_PRICE_DATA = "BITCOIN_PRICE_DATA"
const val TIME_SPAN_ONE_MONTH = "30days"
const val TIME_SPAN_THREE_MONTHS = "90days"
const val TIME_SPAN_ONE_YEAR = "1year"
const val TIME_SPAN_ALL = "all"

class BitcoinPriceViewModel(
    private val repository: RepositoryKit,
    private val toastMaker: ToastMaker,
    private val dataNormalization: DataNormalization,
    private val schedulerProvider: SchedulerProvider,
    private val compositeDisposable: CompositeDisposable
) {
    private val graphVisibility = ObservableInt(View.GONE)
    private val progressBarVisibility = ObservableInt(View.VISIBLE)
    private val marketPrice = ObservableField<MarketPrice>()

    /**
     * retrieve bitcoin price for a particular span from the repository
     */
    private fun getData(timeSpan: TimeSpan) {
        val chartSpan = getTimeSpan(timeSpan)

        compositeDisposable.add(repository.getBitcoinPrice(chartSpan)
            .subscribeOn(schedulerProvider.ioSchedulerProvider())
            .observeOn(schedulerProvider.uiSchedulerProvider())
            .doFinally {
                progressBarVisibility.set(View.GONE)
            }
            .subscribeBy(
                onNext = {
                    marketPrice.set(it)
                    dataNormalization.normalizeData(it)
                    graphVisibility.set(View.VISIBLE)
                },
                onError = {
                    if (it is IOException) {
                        toastMaker.showToast(R.string.error_string_no_internet)
                    } else
                        toastMaker.showToast(R.string.error_string)
                }
            )
        )
    }

    /**
     * onclick event on the other timespan buttons on the screen
     */
    fun onUpdateChartButtonClicked(timeSpan: TimeSpan) {
        progressBarVisibility.set(View.VISIBLE)
        getData(timeSpan)
    }

    /**
     * dispose all observable subscription
     */
    fun dispose() {
        compositeDisposable.clear()
    }

    /**
     * Store data so we can reuse after any configuration change like screen rotation
     */
    fun saveInstanceData(bundle: Bundle?) {
        bundle?.putSerializable(BITCOIN_PRICE_DATA, marketPrice.get())
    }

    /**
     * Restore data to use after any configuration change like screen rotation
     */
    fun onCreate(bundle: Bundle?) {
        val retrieveMarketPrice = bundle?.getSerializable(BITCOIN_PRICE_DATA)

        retrieveMarketPrice?.let {
            marketPrice.set(it as MarketPrice)
            dataNormalization.normalizeData(it).subscribeBy(
                onComplete = {
                    graphVisibility.set(View.VISIBLE)
                    progressBarVisibility.set(View.GONE)
                },
                onError = {
                    toastMaker.showToast(R.string.error_string)
                    progressBarVisibility.set(View.GONE)
                }
            )
        } ?: getData(TimeSpan.OneMonth)
    }

    /**
     * method to get the timeSpan query string parameter to be send to the API based on the given input enum TimeSpan
     */
    private fun getTimeSpan(timeSpan: TimeSpan): String {
        return when (timeSpan) {
            TimeSpan.OneMonth -> {
                TIME_SPAN_ONE_MONTH
            }
            TimeSpan.ThreeMonth -> {
                TIME_SPAN_THREE_MONTHS
            }
            TimeSpan.Year -> {
                TIME_SPAN_ONE_YEAR
            }
            TimeSpan.AllTime -> {
                TIME_SPAN_ALL
            }
        }
    }

    fun getProgressBarVisibility(): ObservableInt {
        return progressBarVisibility
    }

    fun getGraphVisibility(): ObservableInt {
        return graphVisibility
    }

    fun getMarketPrice(): ObservableField<MarketPrice> {
        return marketPrice
    }
}

enum class TimeSpan {
    OneMonth, ThreeMonth, Year, AllTime
}