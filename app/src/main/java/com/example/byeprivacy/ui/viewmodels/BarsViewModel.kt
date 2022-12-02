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

                Log.d("applocation",it.lat.toString())

            }?: emit(listOf())
            emitSource(repository._dbBars(defaultValueSort))
            loading.postValue(false)
        }
    }
/*
    val bars :LiveData<List<BarDbItem>?> =

        liveData {
            loading.postValue(true)
            repository._barList(appLocation.value?.lat!!, appLocation.value!!.lon,{ _message.postValue(EventHandler(it)) })
            loading.postValue(false)
            emitSource(repository._dbBars())
    }
*/
    fun refreshData(sort_val: Int,appLocation: AppLocation){
        defaultValueSort = sort_val
        Log.d("refresg_bars",appLocation.lat.toString())
        viewModelScope.launch {
            loading.postValue(true)
            repository._barList(appLocation.lat, appLocation.lon,{ _message.postValue(EventHandler(it)) })
            loading.postValue(false)
        }
    }

    fun sortByUsers(){
        when (defaultValueSort){
            0 -> return
            1 -> return
            -1 -> return
        }
    }
    fun show(msg: String){ _message.postValue(EventHandler(msg))}

}