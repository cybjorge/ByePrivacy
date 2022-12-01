package com.example.byeprivacy.ui.widgets.friends.list

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.byeprivacy.R
import com.example.byeprivacy.data.db.models.FriendItem
import com.example.byeprivacy.ui.fragments.BarsFragmentDirections
import com.example.byeprivacy.ui.widgets.friends.InterfaceFriends
import com.example.byeprivacy.ui.widgets.friends.list.AdapterFollowing

class RecyclerViewFollowing : RecyclerView{
    private lateinit var adapterFriends: AdapterFollowing

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        adapterFriends = AdapterFollowing(object : InterfaceFriends {
            override fun onFriendClick(id_bar: String) {
                Log.d("Followers bar id",id_bar)
                try {
                    this@RecyclerViewFollowing.findNavController().navigate(
                        BarsFragmentDirections.actionGlobalDetailFragment(id_bar))
                }catch (ex: Exception){
                    this@RecyclerViewFollowing.findNavController().navigate(R.id.action_global_friendsFragment)
                }

            }
        })
        adapter = adapterFriends
    }

}
@BindingAdapter(value = ["followingItems"])
fun RecyclerViewFollowing.applyItems(
    following: List<FriendItem>?
){
    (adapter as AdapterFollowing).items = following ?: emptyList()
    Log.d("following bind",(adapter as AdapterFollowing).items.toString())
}

