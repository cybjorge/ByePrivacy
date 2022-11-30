package com.example.byeprivacy.data.api


data class UserCreateRequest(
    val name: String,
    val password: String
)

data class UserLoginRequest(
    val name: String,
    val password: String
)
data class UserRefreshRequest(
    val refresh: String
)

data class barRequest(
    val id: String,
    val name: String,
    val type: String,
    val lat: Double,
    val lon: Double
)

data class friendRequest(
    val contact: String
)