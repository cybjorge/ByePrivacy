package com.example.byeprivacy.ui.viewmodels

import androidx.lifecycle.*
import com.example.byeprivacy.CheckinBar.checkedBar
import com.example.byeprivacy.CheckinBar.checkedbool
import com.example.byeprivacy.data.LocalRepo
import com.example.byeprivacy.data.db.models.BarApiItem
import com.example.byeprivacy.data.db.models.BarDbItem
import com.example.byeprivacy.utils.AppLocation

import com.example.byeprivacy.utils.EventHandler
import com.google.android.gms.common.api.Api
import kotlinx.coroutines.launch

class BarsWithLocationViewModel (private val repository: LocalRepo): ViewModel() {
    private val _message = MutableLiveData<EventHandler<String>>()
    val message : LiveData<EventHandler<String>>
        get() = _message
    val loading = MutableLiveData(false)

    val appLocation = MutableLiveData<AppLocation>(null)
    val checkinBar = MutableLiveData<BarApiItem>(null)

    private val _checkedIn = MutableLiveData<EventHandler<Boolean>>()
    val checkedIn: LiveData<EventHandler<Boolean>>
        get() = _checkedIn

    val bars :LiveData<List<BarApiItem>?> = appLocation.switchMap {
        liveData {
            loading.postValue(true)
            it?.let {
                val bar = repository._barsInRadius(it.lat,it.lon,{_message.postValue(EventHandler(it))})
                emit(bar)
                if (checkinBar.value == null){
                    if (bar != null) {
                        checkinBar.postValue(bar.firstOrNull())
                    }
                }
            }?: emit(listOf())
            loading.postValue(false)

        }
    }
    fun checkMeIn(){
        viewModelScope.launch {
            loading.postValue(true)
            checkinBar.value?.let {
                repository._barCheckIn(
                    it,
                    {_message.postValue(EventHandler(it))},
                    {_checkedIn.postValue(EventHandler(it))},
                )
                checkedBar=it.name
                checkedbool = true
            }
            loading.postValue(false)

        }
    }

    fun show(msg: String){ _message.postValue(EventHandler(msg))}

}