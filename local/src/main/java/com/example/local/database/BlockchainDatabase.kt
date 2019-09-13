package com.example.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.local.converter.DataTypeConverter
import com.example.local.converter.DateTypeConverter
import com.example.local.dao.PriceDao
import com.example.local.model.MarketPriceRoom

@Database(entities = [MarketPriceRoom::class], version = 1, exportSchema = false)

@TypeConverters(DateTypeConverter::class, DataTypeConverter::class)
abstract class BlockchainDatabase : RoomDatabase() {

    // DAO
    abstract fun priceDao(): PriceDao

    companion object {
        fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, BlockchainDatabase::class.java, "blockchain.db")
                .build()
    }
}