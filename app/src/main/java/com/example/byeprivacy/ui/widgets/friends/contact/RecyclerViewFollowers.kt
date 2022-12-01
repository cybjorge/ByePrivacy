package com.example.byeprivacy.ui.widgets.friends.contact

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.byeprivacy.R
import com.example.byeprivacy.data.db.models.ContactItem
import com.example.byeprivacy.ui.widgets.friends.InterfaceContacts
/*
class RecyclerViewFollowers : RecyclerView {
    private lateinit var adapterFollowers: AdapterFollowers

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        adapter = AdapterFollowers(object : InterfaceContacts {
            override fun onContactClick(username: String) {
                Log.d("Followers bar id", username)
                try {
                }catch (ex: Exception){
                    this@RecyclerViewFollowers.findNavController().navigate(R.id.action_global_friendsFragment)
                }

            }
        })
        adapter = adapterFollowers
    }

}

@BindingAdapter(value = ["followerItems"])
fun RecyclerViewFollowers.applyItems(
    followers: List<ContactItem>?
){
    (adapter as AdapterFollowers).items = followers ?: emptyList()
}



 */