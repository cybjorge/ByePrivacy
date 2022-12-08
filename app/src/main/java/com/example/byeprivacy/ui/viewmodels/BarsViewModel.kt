package com.example.byeprivacy.ui.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.byeprivacy.data.LocalRepo
import com.example.byeprivacy.data.db.models.BarApiItem
import com.example.byeprivacy.data.db.models.BarDbItem
import com.example.byeprivacy.utils.AppLocation
import com.example.byeprivacy.utils.EventHandler
import kotlinx.coroutines.launch

class BarsViewModel(private val repository: LocalRepo) : ViewModel() {

    private var defaultValueSort: Int  = 0
    private val _message = MutableLiveData<EventHandler<String>>()
    val message : LiveData<EventHandler<String>>
        get() = _message
    val loading = MutableLiveData(false)
    val appLocation = MutableLiveData<AppLocation>(null)

    val bars :LiveData<List<BarDbItem>?> = appLocation.switchMap {
        liveData {
            loading.postValue(true)
            it?.let {
                val bar = repository._barList(it.lat,it.lon,{_message.postValue(EventHandler(it))})
                emit(bar)
            }?: emit(listOf())
            emitSource(repository._dbBars(defaultValueSort))
            loading.postValue(false)
        }
    }

    fun refreshData(sort_val: Int,appLocation: AppLocation){
        defaultValueSort = sort_val
        viewModelScope.launch {
            loading.postValue(true)
            repository._barList(appLocation.lat, appLocation.lon,{ _message.postValue(EventHandler(it)) })
            loading.postValue(false)
        }
    }

    fun show(msg: String){ _message.postValue(EventHandler(msg))}

}