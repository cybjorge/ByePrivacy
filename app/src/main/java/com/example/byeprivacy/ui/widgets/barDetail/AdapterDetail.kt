package com.example.byeprivacy.ui.widgets.barDetail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.byeprivacy.R
import com.example.byeprivacy.utils.autoNotify
import kotlin.properties.Delegates

class AdapterDetail() : RecyclerView.Adapter<AdapterDetail.BarItemDetailViewHolder>() {
    var items: List<BarItemDetail> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o.key.compareTo(n.key) == 0 }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarItemDetailViewHolder {
        return BarItemDetailViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BarItemDetailViewHolder, position: Int) {
        holder.bind(items[position])
    }
    class BarItemDetailViewHolder(
        private val parent: ViewGroup,
        itemView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.bar_detail_item,
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: BarItemDetail) {
            itemView.findViewById<TextView>(R.id.name).text = item.key
            itemView.findViewById<TextView>(R.id.value).text = item.value
        }
    }
}