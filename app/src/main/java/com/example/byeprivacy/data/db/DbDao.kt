package com.example.byeprivacy.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.byeprivacy.data.db.models.BarDbItem
import com.example.byeprivacy.data.db.models.ContactItem
import com.example.byeprivacy.data.db.models.FriendItem

@Dao
interface DbDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBars(bars: List<BarDbItem>)

    @Query("DELETE FROM bars")
    suspend fun deleteBars()

    @Query("SELECT * FROM bars order by users DESC, name ASC")
    fun getBars(): LiveData<List<BarDbItem>?>

    @Query("SELECT users FROM bars WHERE id = :bar_id")
    fun getBarsValue(bar_id: String): Int


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriends(friends: List<FriendItem>)

    @Query("DELETE FROM friends")
    suspend fun deleteFriends()

    @Query("SELECT * FROM friends")
    fun getFriends(): LiveData<List<FriendItem>?>

/*
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContacts(contact: List<ContactItem>)

    @Query("DELETE FROM contacts")
    suspend fun deleteContacts()

    @Query("SELECT * FROM contacts")
    fun getContacts(): LiveData<List<ContactItem>?>
*/

    @Query("SELECT * FROM bars ORDER BY name ASC")
    fun sortBarsByNameAsc(): LiveData<List<BarDbItem>?>

    @Query("SELECT * FROM bars ORDER BY name DESC")
    fun sortBarsByNameDesc(): LiveData<List<BarDbItem>?>

    @Query("SELECT * FROM bars ORDER BY users ASC")
    fun sortBarsByUsersAsc(): LiveData<List<BarDbItem>?>

    @Query("SELECT * FROM bars ORDER BY users DESC")
    fun sortBarsByUsersDesc(): LiveData<List<BarDbItem>?>

    @Query("SELECT * FROM bars ORDER BY distance ASC")
    fun sortBarsByDistsAsc(): LiveData<List<BarDbItem>?>

    @Query("SELECT * FROM bars ORDER BY distance DESC")
    fun sortBarsByDistsDesc(): LiveData<List<BarDbItem>?>
}