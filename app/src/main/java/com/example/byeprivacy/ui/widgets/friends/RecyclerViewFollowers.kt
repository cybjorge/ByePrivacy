package com.example.byeprivacy.ui.widgets.friends

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.byeprivacy.data.db.models.FriendItem
import com.example.byeprivacy.ui.fragments.BarsFragmentDirections
class RecyclerViewFollowers : RecyclerView{
    private lateinit var adapterFriends: AdapterFollowers

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        adapterFriends = AdapterFollowers(object : InterfaceFriends {
            override fun onFriendClick(id_bar: String) {
                this@RecyclerViewFollowers.findNavController().navigate(
                    BarsFragmentDirections.actionGlobalDetailFragment(id_bar))
                Log.d("Followers bar id",id_bar)
            }
        })
        adapter = adapterFriends
    }

}
@BindingAdapter(value = ["followerItems"])
fun RecyclerViewFollowers.applyItems(
    followers: List<FriendItem>?
){
    (adapter as AdapterFollowers).items = followers ?: emptyList()
}