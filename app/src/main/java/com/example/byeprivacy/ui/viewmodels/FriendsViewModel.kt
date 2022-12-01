package com.example.byeprivacy.ui.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.byeprivacy.data.LocalRepo
import com.example.byeprivacy.data.db.models.BarDbItem
import com.example.byeprivacy.data.db.models.FriendItem
import com.example.byeprivacy.utils.EventHandler
import kotlinx.coroutines.launch

class FriendsViewModel(private val repository: LocalRepo) : ViewModel(){
    private val _message = MutableLiveData<EventHandler<String>>()
    val message : LiveData<EventHandler<String>>
        get() = _message
    val loading = MutableLiveData(false)


    private val _addedFriend = MutableLiveData<EventHandler<Boolean>>()
    val addedFriend: LiveData<EventHandler<Boolean>>
        get() = _addedFriend

    val friendListFollowing :LiveData<List<FriendItem>?> =
        liveData {
            loading.postValue(true)
            repository._getFollowingFriends{_message.postValue(EventHandler(it))}
            loading.postValue(false)
            emitSource(repository._dbFriends())
        }

    val friendListFollowers :LiveData<List<FriendItem>?> =
        liveData {
            loading.postValue(true)
            repository._getFollowersFriends{_message.postValue(EventHandler(it))}
            loading.postValue(false)
        }


    fun getFriendsFollowing(){
        //fucntion uses data from GET https://zadanie.mpage.sk/contact/list.php
        viewModelScope.launch {
            loading.postValue(true)
            repository._getFollowingFriends { _message.postValue(EventHandler(it)) }
            loading.postValue(false)
        }
    }
    fun getFriendsFollowes(){
        //fucntion uses data from GET https://zadanie.mpage.sk/contact/friends.php

        viewModelScope.launch {
            loading.postValue(true)
            repository._getFollowersFriends { _message.postValue(EventHandler(it)) }
            loading.postValue(false)
        }
    }
    fun refreshDataFollowing(){
        Log.d("following refresh tostring ",friendListFollowing.toString())

        viewModelScope.launch {
            loading.postValue(true)
            repository._getFollowingFriends { _message.postValue(EventHandler(it)) }
            loading.postValue(false)
        }
    }

    fun refreshDataFollowers(){
        Log.d(null,"refreshFollowers")
        viewModelScope.launch {
            loading.postValue(true)
            repository._getFollowersFriends { _message.postValue(EventHandler(it)) }
            loading.postValue(false)
        }
    }
    fun addFriend(friend_name: String){
        viewModelScope.launch {
            loading.postValue(true)
            repository._addFriend(friend_name,{_message.postValue(EventHandler(it))},{_addedFriend.postValue(EventHandler(it))})
            loading.postValue(false)
        }
    }

    fun removeFriend(){}

    fun show(msg: String){ _message.postValue(EventHandler(msg))}

}