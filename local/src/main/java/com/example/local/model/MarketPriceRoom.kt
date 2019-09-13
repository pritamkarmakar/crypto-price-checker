package com.example.local.model

import androidx.room.*
import com.example.model.MarketPrice

@Entity(tableName = "bitcoinPrice")
data class MarketPriceRoom(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @Embedded
    val marketPrice: MarketPrice
)