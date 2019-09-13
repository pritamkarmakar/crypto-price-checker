package com.example.blockchain

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bitcoinpricechart.view.BitcoinPriceActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigateToBitcoinPrice()
    }

    /**
     * navigate to BitcoinPriceActivity
     */
    fun navigateToBitcoinPrice(){
        startActivity(Intent(this, BitcoinPriceActivity::class.java))
    }
}
