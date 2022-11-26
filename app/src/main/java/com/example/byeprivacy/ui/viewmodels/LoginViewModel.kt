package com.example.byeprivacy.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.byeprivacy.data.LocalRepo
import com.example.byeprivacy.data.api.UserResponse
import com.example.byeprivacy.utils.EventHandler
import kotlinx.coroutines.launch

class LoginViewModel (private val repository: LocalRepo) : ViewModel() {
    private val _message = MutableLiveData<EventHandler<String>>()
    val message : LiveData<EventHandler<String>>
    get() = _message

    val user = MutableLiveData<UserResponse>(null)
    val loading = MutableLiveData(false)

    fun login(name:String, password: String) {
        viewModelScope.launch {
            loading.postValue(true)
            repository._userLogIn(
                name,password,
                {
                    _message.postValue(EventHandler(it))
                },
                {
                    user.postValue(it)
                }
            )
            loading.postValue(false)
        }
    }
    fun show(msg: String){ _message.postValue(EventHandler(msg))}


}