package com.example.myapplication

data class RawData(
    val n1: Int,
    val n2: Int,
    val n3: Int,
    val n4: Int,
    val n5: Int,
    val n6: Int
)

data class SensorDataClass(
    val temperature: Int,
    val moisture: Int,
    val xVal: Int,
    val yVal: Int
)