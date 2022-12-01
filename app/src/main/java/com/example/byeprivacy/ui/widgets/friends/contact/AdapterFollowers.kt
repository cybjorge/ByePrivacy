package com.example.byeprivacy.ui.widgets.friends.contact

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.byeprivacy.R
import com.example.byeprivacy.data.db.models.ContactItem
import com.example.byeprivacy.ui.widgets.friends.InterfaceContacts
import com.example.byeprivacy.utils.autoNotify
import kotlin.properties.Delegates
class AdapterFollowers(val events: InterfaceContacts?=null):
    RecyclerView.Adapter<AdapterFollowers.FriendItemViewHolder>() {

    var items: List<ContactItem> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o.user_id.compareTo(n.user_id) == 0 }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendItemViewHolder {
        return FriendItemViewHolder(parent)
    }
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: FriendItemViewHolder, position: Int) {
        holder.bind(items[position],events)
    }
    class FriendItemViewHolder(
        private val parent: ViewGroup,
        itemView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.contact_item,
            parent,
            false
        )
    ):RecyclerView.ViewHolder(itemView){
        fun bind(item: ContactItem, events: InterfaceContacts?){
            Log.d("followerItemFromAdatpter",item.toString())
            itemView.findViewById<TextView>(R.id.contact_username).text=item.user_name
            itemView.findViewById<Button>(R.id.contact_delete).setOnClickListener { events?.onContactClick(item.user_name) }
        }

    }


}
