package com.example.byeprivacy.ui.widgets.friends

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.byeprivacy.R
import com.example.byeprivacy.data.db.models.BarDbItem
import com.example.byeprivacy.data.db.models.FriendItem
import com.example.byeprivacy.ui.widgets.bars.InterfaceBars
import com.example.byeprivacy.utils.autoNotify
import kotlin.properties.Delegates

class AdapterFriends(val events: InterfaceFriends?=null):
    RecyclerView.Adapter<AdapterFriends.FriendItemViewHolder>() {
    var followers: List<FriendItem> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o.user_id.compareTo(n.user_id) == 0 }
    }
    var following: List<FriendItem> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o.user_id.compareTo(n.user_id) == 0 }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendItemViewHolder {
        return FriendItemViewHolder(parent)
    }

    fun getFollowersItemCount(): Int {
        return followers.size
    }
    fun getFollowingItemCount(): Int {
        return followers.size
    }

    override fun onBindViewHolder(holder: FriendItemViewHolder, position: Int) {
        holder.bindFollower(followers[position],events)
        holder.bindFollowing(following[position],events)
    }
    class FriendItemViewHolder(
        private val parent: ViewGroup,
        itemView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.friend_item,
            parent,
            false
        )
    ):RecyclerView.ViewHolder(itemView){
        fun bindFollower(item: FriendItem, events: InterfaceFriends?){
            Log.d("followerItemFromAdatpter",item.toString())
            itemView.findViewById<TextView>(R.id.friend_username).text=item.user_name
            itemView.findViewById<TextView>(R.id.friend_bar_name).text=item.bar_name
            itemView.setOnClickListener{ events?.onFriendClick(item.bar_id!!) }

        }
        fun bindFollowing(item: FriendItem, events: InterfaceFriends?){
            Log.d("followingItemFromAdatpter",item.toString())

            itemView.findViewById<TextView>(R.id.friend_username).text=item.user_name
            itemView.findViewById<TextView>(R.id.friend_bar_name).text=item.bar_name
            itemView.setOnClickListener{ item.bar_id?.let { it1 -> events?.onFriendClick(it1) } }
        }
    }

    override fun getItemCount(): Int {
        return followers.size
    }
}