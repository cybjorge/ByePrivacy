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
data class BarItemDetailResponse(
    val id: String,
    val type: String,
    val lat: Double,
    val lon: Double,
    val tags: Map<String,String>
)

data class BarDetailResponse(
    val elements: List<BarItemDetailResponse>
)

data class FriendResponse(
    val user_id: String,
    val user_name: String,
    val bar_id: String?,
    val bar_name: String?,
    val time: String?,
    val bar_lat: Double?,
    val bar_lon: Double?
)