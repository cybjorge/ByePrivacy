package com.example.byeprivacy.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.byeprivacy.data.LocalRepo
import com.example.byeprivacy.data.db.LocalCache
import com.example.byeprivacy.ui.viewmodels.*

class ViewModelFactory(private val repository: LocalRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SignUpViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(BarsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BarsViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(BarsWithLocationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BarsWithLocationViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(FriendsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FriendsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}