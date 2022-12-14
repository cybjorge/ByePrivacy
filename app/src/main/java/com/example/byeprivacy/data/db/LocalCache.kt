package com.example.byeprivacy.data.db

import androidx.lifecycle.LiveData
import com.example.byeprivacy.data.db.models.BarApiItem
import com.example.byeprivacy.data.db.models.BarDbItem
import com.example.byeprivacy.data.db.models.ContactItem
import com.example.byeprivacy.data.db.models.FriendItem

class LocalCache(private val dao: DbDao) {
    suspend fun insertBars(bars: List<BarDbItem>){
        dao.insertBars(bars)
    }
    suspend fun deleteBars(){ dao.deleteBars() }

    fun getBars(): LiveData<List<BarDbItem>?> = dao.getBars()

    fun getBarItem(bar: String) : Int = dao.getBarsValue(bar)

    suspend fun insertFriends(friends: List<FriendItem>){
        dao.insertFriends(friends)
    }
    suspend fun deleteFriends(){ dao.deleteFriends() }

    fun getFriends(): LiveData<List<FriendItem>?> = dao.getFriends()

    fun getBarsSortDistDesc(): LiveData<List<BarDbItem>?> = dao.sortBarsByDistsDesc()
    fun getBarsSortDistAsc(): LiveData<List<BarDbItem>?> = dao.sortBarsByDistsAsc()

    fun getBarsSortUserDesc(): LiveData<List<BarDbItem>?> = dao.sortBarsByUsersDesc()
    fun getBarsSortUserAsc(): LiveData<List<BarDbItem>?> = dao.sortBarsByUsersAsc()

    fun getBarsSortNameDesc(): LiveData<List<BarDbItem>?> = dao.sortBarsByNameDesc()
    fun getBarsSortNameAsc(): LiveData<List<BarDbItem>?> = dao.sortBarsByNameAsc()
}