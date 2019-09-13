package com.example.bitcoinpricechart.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.bitcoinpricechart.R
import com.example.bitcoinpricechart.databinding.ActivityBitcoinPriceBinding
import com.example.bitcoinpricechart.di.component.DaggerBitcoinPriceComponent
import com.example.bitcoinpricechart.di.component.FeatureDependencies
import com.example.bitcoinpricechart.di.component.FeatureDependenciesProvider
import com.example.bitcoinpricechart.viewmodel.BitcoinPriceViewModel
import com.example.bitcoinpricechart.DataNormalization
import javax.inject.Inject

class BitcoinPriceActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModel: BitcoinPriceViewModel
    @Inject
    lateinit var dataNormalization: DataNormalization

    private lateinit var binding: ActivityBitcoinPriceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setupDI()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bitcoin_price)
        binding.vm = viewModel
        binding.dataNormalization = dataNormalization
        viewModel.onCreate(savedInstanceState)
        // change the chart x axis formatting, to display user friendly time
        val xAxis = binding.graphView.xAxis
        xAxis.valueFormatter = DateAxisFormatter()
    }

    private fun setupDI() {
        val dependencies: FeatureDependencies =
            (applicationContext as FeatureDependenciesProvider).provideFeatureDependencies()
        val component = DaggerBitcoinPriceComponent
            .builder()
            .dependencies(dependencies)
            .build()
        component.inject(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.dispose()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveInstanceData(outState)
    }
}
