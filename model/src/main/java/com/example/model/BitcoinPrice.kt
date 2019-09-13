package com.example.model

import java.io.Serializable

data class MarketPrice(
    val description: String,
    val name: String,
    val period: String,
    val status: String,
    val unit: String,
    val values: MutableList<Value>
) : Serializable

data class Value(
    val x: Float,
    val y: Float
) : Serializable