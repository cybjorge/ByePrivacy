package com.example.byeprivacy.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.byeprivacy.data.db.models.BarDbItem
import com.example.byeprivacy.data.db.models.ContactItem
import com.example.byeprivacy.data.db.models.FriendItem


@Database(entities = [BarDbItem::class,FriendItem::class], exportSchema = false, version = 1)
abstract class RoomDatabaseBP: RoomDatabase() {
    abstract fun appDao(): DbDao

    companion object{
        @Volatile
        private var INSTANCE: RoomDatabaseBP? = null

        fun getInstance(context: Context): RoomDatabaseBP =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {  INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                RoomDatabaseBP::class.java, "barsDatabase"
            ).fallbackToDestructiveMigration()
                .build()
    }
}