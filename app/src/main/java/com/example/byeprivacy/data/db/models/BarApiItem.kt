package com.example.byeprivacy.data.db.models

import android.location.Location
import com.example.byeprivacy.utils.AppLocation

class BarApiItem(
    val id: String,
    val name: String,
    val type: String,
    val lat: Double,
    val lon: Double,
    val tags: Map<String, String>,
    var users: String,
    var distance: Double = 0.0


) {
    fun distanceTo(location: AppLocation): Double{
        return Location("").apply {
            latitude=lat
            longitude=lon
        }.distanceTo(Location("").apply {
            latitude=location.lat
            longitude=location.lon
        }).toDouble()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BarApiItem

        if (id != other.id) return false
        if (name != other.name) return false
        if (type != other.type) return false
        if (lat != other.lat) return false
        if (lon != other.lon) return false
        if (tags != other.tags) return false
        if (users != other.users) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + lat.hashCode()
        result = 31 * result + lon.hashCode()
        result = 31 * result + tags.hashCode()
        result = 31 * result + users.hashCode()
        return result
    }


}