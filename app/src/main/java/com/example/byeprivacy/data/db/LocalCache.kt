package com.example.byeprivacy.data.db

import androidx.lifecycle.LiveData
import com.example.byeprivacy.data.db.models.BarDbItem
import com.example.byeprivacy.data.db.models.ContactItem
import com.example.byeprivacy.data.db.models.FriendItem

class LocalCache(private val dao: DbDao) {
    suspend fun insertBars(bars: List<BarDbItem>){
        dao.insertBars(bars)
    }
    suspend fun deleteBars(){ dao.deleteBars() }

    fun getBars(): LiveData<List<BarDbItem>?> = dao.getBars()


    suspend fun insertFriends(friends: List<FriendItem>){
        dao.insertFriends(friends)
    }
    suspend fun deleteFriends(){ dao.deleteFriends() }

    fun getFriends(): LiveData<List<FriendItem>?> = dao.getFriends()


    suspend fun insertContacts(contacts: List<ContactItem>){
        dao.insertContacts(contacts)
    }
    suspend fun deleteContacts(){ dao.deleteContacts() }

    fun getContacts(): LiveData<List<ContactItem>?> = dao.getContacts()
}