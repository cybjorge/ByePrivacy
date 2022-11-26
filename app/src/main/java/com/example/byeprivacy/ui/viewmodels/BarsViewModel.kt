package com.example.byeprivacy.ui.viewmodels

import androidx.lifecycle.*
import com.example.byeprivacy.data.LocalRepo
import com.example.byeprivacy.data.db.models.BarDbItem
import com.example.byeprivacy.utils.EventHandler
import kotlinx.coroutines.launch

class BarsViewModel(private val repository: LocalRepo) : ViewModel() {
    private val _message = MutableLiveData<EventHandler<String>>()
    val message : LiveData<EventHandler<String>>
        get() = _message
    val loading = MutableLiveData(false)

    val bars :LiveData<List<BarDbItem>?> =
        liveData {
            loading.postValue(true)
            repository._barList{_message.postValue(EventHandler(it))}
            loading.postValue(false)
            emitSource(repository._dbBars())
        }
    fun refreshData(){
        viewModelScope.launch {
            loading.postValue(true)
            repository._barList { _message.postValue(EventHandler(it)) }
            loading.postValue(false)
        }
    }
    fun show(msg: String){ _message.postValue(EventHandler(msg))}

}