package com.example.byeprivacy.ui.widgets.friends.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.byeprivacy.R
import com.example.byeprivacy.data.db.models.FriendItem
import com.example.byeprivacy.ui.widgets.friends.InterfaceFriends
import com.example.byeprivacy.utils.autoNotify
import kotlin.properties.Delegates

class AdapterFollowing(val events: InterfaceFriends?=null):
    RecyclerView.Adapter<AdapterFollowing.FriendItemViewHolder>() {
    var items: List<FriendItem> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o.user_id.compareTo(n.user_id) == 0 }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendItemViewHolder {
        return FriendItemViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FriendItemViewHolder, position: Int) {
        Log.d("items friends",items.toString())

        holder.bind(items[position], events)
    }

    class FriendItemViewHolder(
        private val parent: ViewGroup,
        itemView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.friend_item,
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: FriendItem, events: InterfaceFriends?) {
            Log.d("followerItemFromAdatpterFollowing", item.toString())
            itemView.findViewById<TextView>(R.id.friend_username).text = item.user_name
            itemView.findViewById<TextView>(R.id.friend_bar_name).text = item.bar_name
            if (item.bar_id!=null){
                itemView.setOnClickListener { events?.onFriendClick(item.bar_id) }
            }
        }

    }
}


