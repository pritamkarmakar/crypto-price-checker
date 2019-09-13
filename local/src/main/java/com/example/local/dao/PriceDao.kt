package com.example.local.dao

import com.example.local.model.MarketPriceRoom
import io.reactivex.Maybe

@androidx.room.Dao
interface PriceDao {
    @androidx.room.Query("SELECT * FROM bitcoinPrice WHERE id  IN (:id)")
    fun getBitcoinPrice(id:String): Maybe<MarketPriceRoom>

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    fun insert(marketPrice: MarketPriceRoom)
}