package com.example.byeprivacy.ui.widgets.bars

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.byeprivacy.R
import com.example.byeprivacy.data.db.models.BarDbItem
import com.example.byeprivacy.utils.autoNotify
import kotlin.properties.Delegates

class AdapterBars(val events: InterfaceBars?=null):
    RecyclerView.Adapter<AdapterBars.BarItemViewHolder>() {
    var items: List<BarDbItem> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o.id.compareTo(n.id) == 0 }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarItemViewHolder {
        return BarItemViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BarItemViewHolder, position: Int) {
        holder.bind(items[position], events)

    }

    class BarItemViewHolder(
        private val parent: ViewGroup,
        itemView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.bar_item,
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(item: BarDbItem, events: InterfaceBars?) {
            itemView.findViewById<TextView>(R.id.name).text = item.name
            itemView.findViewById<TextView>(R.id.count).text = item.users.toString()+" visitors"
            itemView.findViewById<TextView>(R.id.dstnc).text = "%.2f m".format(item.distance)

            when (item.type) {
                "pub" -> itemView.findViewById<ImageView>(R.id.type)
                    .setImageResource(R.drawable.pub)
                "bar" -> itemView.findViewById<ImageView>(R.id.type)
                    .setImageResource(R.drawable.baar)
                "fast_food" -> itemView.findViewById<ImageView>(R.id.type)
                    .setImageResource(R.drawable.fastfood)
                "nightclub" -> itemView.findViewById<ImageView>(R.id.type)
                    .setImageResource(R.drawable.restaurant)
                "restaurant" -> itemView.findViewById<ImageView>(R.id.type)
                    .setImageResource(R.drawable.restaurant)
                "stripclub" -> itemView.findViewById<ImageView>(R.id.type)
                    .setImageResource(R.drawable.stripclub)
                "cafe" -> itemView.findViewById<ImageView>(R.id.type)
                    .setImageResource(R.drawable.cafe)
                else -> itemView.findViewById<ImageView>(R.id.type)
                    .setImageResource(R.drawable.uknown)
            }

            itemView.setOnClickListener { events?.onBarClick(item) }
        }
    }
}