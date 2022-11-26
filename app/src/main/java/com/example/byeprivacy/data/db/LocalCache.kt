package com.example.byeprivacy.data.db

import androidx.lifecycle.LiveData
import com.example.byeprivacy.data.db.models.BarDbItem

class LocalCache(private val dao: DbDao) {
    suspend fun insertBars(bars: List<BarDbItem>){
        dao.insertBars(bars)
    }
    suspend fun deleteBars(){ dao.deleteBars() }

    fun getBars(): LiveData<List<BarDbItem>?> = dao.getBars()
}