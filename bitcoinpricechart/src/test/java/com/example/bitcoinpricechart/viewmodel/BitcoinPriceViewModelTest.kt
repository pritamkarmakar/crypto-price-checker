package com.example.bitcoinpricechart.viewmodel

import android.os.Bundle
import android.view.View
import com.example.basekit.ToastMaker
import com.example.bitcoinpricechart.DataNormalization
import com.example.bitcoinpricechart.R
import com.example.bitcoinpricechart.viewmodel.utils.TestSchedulerProvider
import com.example.model.MarketPrice
import com.example.repokit.RepositoryKit
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.junit.Assert
import org.junit.Test
import org.junit.Before
import org.mockito.ArgumentMatchers
import java.io.IOException

class BitcoinPriceViewModelTest {
    private lateinit var viewModel: BitcoinPriceViewModel
    private val toastMaker: ToastMaker = mock()
    private val repositoryKit: RepositoryKit = mock()
    private val bundle: Bundle = mock()
    private val compositeDisposable: CompositeDisposable = mock()
    private val dataNormalization: DataNormalization = mock()
    private val schedulerProvider = TestSchedulerProvider()
    private val testMarketPrice = MarketPrice("desc", "name", "period", "status", "unit", mutableListOf())

    @Before
    fun setUp() {
        whenever(repositoryKit.getBitcoinPrice(any())).thenReturn(Observable.just(testMarketPrice))
        viewModel =
            BitcoinPriceViewModel(repositoryKit, toastMaker, dataNormalization, schedulerProvider, compositeDisposable)
    }

    @Test
    fun `verify updateRange with time span as OneMonth`() {
        viewModel.onUpdateChartButtonClicked(TimeSpan.OneMonth)
        verify(repositoryKit, times(1)).getBitcoinPrice(TIME_SPAN_ONE_MONTH)
        Assert.assertEquals(testMarketPrice, viewModel.getMarketPrice().get())
        visibilityVerification(progressbasVisibility = View.GONE, graphVisibility = View.VISIBLE)
    }

    @Test
    fun `verify updateRange with time span as ThreeMonth`() {
        viewModel.onUpdateChartButtonClicked(TimeSpan.ThreeMonth)
        verify(repositoryKit, times(1)).getBitcoinPrice(TIME_SPAN_THREE_MONTHS)
        Assert.assertEquals(testMarketPrice, viewModel.getMarketPrice().get())
        visibilityVerification(progressbasVisibility = View.GONE, graphVisibility = View.VISIBLE)
    }

    @Test
    fun `verify updateRange with time span as Year`() {
        viewModel.onUpdateChartButtonClicked(TimeSpan.Year)
        verify(repositoryKit, times(1)).getBitcoinPrice(TIME_SPAN_ONE_YEAR)
        Assert.assertEquals(testMarketPrice, viewModel.getMarketPrice().get())
        visibilityVerification(progressbasVisibility = View.GONE, graphVisibility = View.VISIBLE)
    }


    @Test
    fun `verify updateRange with time span as AllTime`() {
        viewModel.onUpdateChartButtonClicked(TimeSpan.AllTime)
        verify(repositoryKit, times(1)).getBitcoinPrice(TIME_SPAN_ALL)
        Assert.assertEquals(testMarketPrice, viewModel.getMarketPrice().get())
        visibilityVerification(progressbasVisibility = View.GONE, graphVisibility = View.VISIBLE)
    }

    @Test
    fun `verify updateRange on IOException`() {
        whenever(repositoryKit.getBitcoinPrice(any())).thenReturn(Observable.error(IOException()))
        viewModel.onUpdateChartButtonClicked(TimeSpan.AllTime)
        verify(toastMaker, times(1)).showToast(R.string.error_string_no_internet)
        visibilityVerification(progressbasVisibility = View.GONE, graphVisibility = View.GONE)
    }

    @Test
    fun `verify updateRange on non IOException`() {
        whenever(repositoryKit.getBitcoinPrice(any())).thenReturn(Observable.error(Exception()))
        viewModel.onUpdateChartButtonClicked(TimeSpan.AllTime)
        verify(toastMaker, times(1)).showToast(R.string.error_string)
        visibilityVerification(progressbasVisibility = View.GONE, graphVisibility = View.GONE)
    }

    @Test
    fun `verify ProgressBar default visibility`() {
        assert(viewModel.getProgressBarVisibility().get() == View.VISIBLE)
    }

    @Test
    fun `verify Graph default visibility`() {
        assert(viewModel.getGraphVisibility().get() == View.GONE)
    }

    @Test
    fun `verify marketprice default visibility`() {
        Assert.assertEquals(viewModel.getMarketPrice().get(), null)
    }

    @Test
    fun dispose() {
        viewModel.dispose()
        verify(compositeDisposable, times(1)).clear()
    }

    @Test
    fun `verify onCreate when there is no previous saved data`() {
        viewModel.onCreate(bundle)
        verify(repositoryKit, times(1)).getBitcoinPrice(any())
    }

    @Test
    fun `verify onCreate when there is previous saved data`() {
        whenever(bundle.getSerializable(BITCOIN_PRICE_DATA)).thenReturn(testMarketPrice)
        whenever(dataNormalization.normalizeData(any())).thenReturn(Completable.complete())
        viewModel.onCreate(bundle)
        verify(repositoryKit, never()).getBitcoinPrice(any())
        visibilityVerification(progressbasVisibility = View.GONE, graphVisibility = View.VISIBLE)
    }

    @Test
    fun `verify onCreate when there is previous saved data but dataNormalization returns error`() {
        whenever(bundle.getSerializable(BITCOIN_PRICE_DATA)).thenReturn(testMarketPrice)
        whenever(dataNormalization.normalizeData(any())).thenReturn(Completable.error(Throwable()))
        viewModel.onCreate(bundle)
        verify(repositoryKit, never()).getBitcoinPrice(any())
        visibilityVerification(progressbasVisibility = View.GONE, graphVisibility = View.GONE)
        verify(toastMaker, times(1)).showToast(R.string.error_string)
    }

    @Test
    fun onSaveInstanceState() {
        viewModel.saveInstanceData(bundle)
        verify(bundle, times(1)).putSerializable(ArgumentMatchers.any(), ArgumentMatchers.any())
    }

    private fun visibilityVerification(progressbasVisibility: Int, graphVisibility: Int){
        Assert.assertEquals(progressbasVisibility, viewModel.getProgressBarVisibility().get())
        Assert.assertEquals(graphVisibility, viewModel.getGraphVisibility().get())
    }
}