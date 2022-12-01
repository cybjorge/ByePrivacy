package com.example.byeprivacy.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.byeprivacy.data.db.models.BarDbItem
import com.example.byeprivacy.data.db.models.FriendItem

@Dao
interface DbDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBars(bars: List<BarDbItem>)

    @Query("DELETE FROM bars")
    suspend fun deleteBars()

    @Query("SELECT * FROM bars order by users DESC, name ASC")
    fun getBars(): LiveData<List<BarDbItem>?>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriends(friends: List<FriendItem>)

    @Query("DELETE FROM friends")
    suspend fun deleteFriends()

    @Query("SELECT * FROM friends")
    fun getFriends(): LiveData<List<FriendItem>?>
}