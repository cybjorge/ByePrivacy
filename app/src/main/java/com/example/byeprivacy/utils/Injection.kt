package com.example.byeprivacy.utils

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.byeprivacy.data.LocalRepo
import com.example.byeprivacy.data.api.RestApi
import com.example.byeprivacy.data.db.LocalCache
import com.example.byeprivacy.data.db.RoomDatabaseBP

object Injection {
    private fun provideCache(context: Context): LocalCache {
        val database = RoomDatabaseBP.getInstance(context)
        return LocalCache(database.appDao())
    }

    fun provideDataRepository(context: Context): LocalRepo {
        return LocalRepo.getInstance(RestApi.create(context), provideCache(context))
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(
            provideDataRepository(
                context
            )
        )
    }
}