package com.example.byeprivacy.ui.viewmodels

import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.V
import android.util.Log
import androidx.lifecycle.*
import com.example.byeprivacy.data.LocalRepo
import com.example.byeprivacy.data.db.DbDao
import com.example.byeprivacy.data.db.LocalCache
import com.example.byeprivacy.data.db.models.BarApiItem
import com.example.byeprivacy.ui.widgets.barDetail.BarItemDetail
import com.example.byeprivacy.utils.EventHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: LocalRepo) : ViewModel() {
    private val _message = MutableLiveData<EventHandler<String>>()
    val message : LiveData<EventHandler<String>>
        get() = _message
    val loading = MutableLiveData(false)


    val bar = MutableLiveData<BarApiItem>(null)
    val type= bar.map { it?.tags?.getOrDefault("amenity","")?:"" }
    val details: LiveData<List<BarItemDetail>> = bar.switchMap {
        liveData {
            it?.let {
                emit(it.tags.map {
                    BarItemDetail(it.key,it.value)
                })
            }?: emit(emptyList<BarItemDetail>())
        }


    }

    fun loadBarDetail(id: String){
        if (id.isNotBlank()){
            CoroutineScope(Dispatchers.IO).launch {
                loading.postValue(true)
                bar.postValue(repository._barDetail(id) {_message.postValue(EventHandler(it))})
                loading.postValue(false)
            }

        }
    }

}