package com.example.byeprivacy.data.api

data class UserResponse(
    val uid: String,
    val access: String,
    val refresh: String
)
data class barResponseList(
    val bar_id: String,
    val bar_name: String,
    val bar_type: String,
    val lat: Double,
    val lon: Double,
    val users: Int
)