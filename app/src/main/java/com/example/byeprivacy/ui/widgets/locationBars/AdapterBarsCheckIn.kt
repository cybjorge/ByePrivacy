package com.example.byeprivacy.ui.widgets.locationBars

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.byeprivacy.R
import com.example.byeprivacy.data.db.models.BarApiItem
import com.example.byeprivacy.utils.autoNotify
import com.google.android.material.chip.Chip
import kotlin.properties.Delegates

class AdapterBarsCheckIn (val events: InterfaceBarsCheckIn?=null):
    RecyclerView.Adapter<AdapterBarsCheckIn.BarCheckInItemViewHolder>() {
    var items: List<BarApiItem> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o.id.compareTo(n.id) == 0 }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarCheckInItemViewHolder {
        return BarCheckInItemViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class BarCheckInItemViewHolder(
        private val parent: ViewGroup,
        itemView : View = LayoutInflater.from(parent.context).inflate(
            R.layout.checkin_bar_item,
            parent,
            false)
    ) : RecyclerView.ViewHolder(itemView){

        @SuppressLint("SetTextI18n")
        fun bind(item: BarApiItem, events: InterfaceBarsCheckIn? = null) {
            itemView.findViewById<TextView>(R.id.name).text = item.name
            itemView.findViewById<TextView>(R.id.distance).text = "%.2f m".format(item.distance)
            when (item.type) {
                "pub" -> itemView.findViewById<ImageView>(R.id.type_checkin)
                    .setImageResource(R.drawable.pub)
                "bar" -> itemView.findViewById<ImageView>(R.id.type_checkin)
                    .setImageResource(R.drawable.baar)
                "fast_food" -> itemView.findViewById<ImageView>(R.id.type_checkin)
                    .setImageResource(R.drawable.fastfood)
                "nightclub" -> itemView.findViewById<ImageView>(R.id.type_checkin)
                    .setImageResource(R.drawable.restaurant)
                "restaurant" -> itemView.findViewById<ImageView>(R.id.type_checkin)
                    .setImageResource(R.drawable.restaurant)
                "stripclub" -> itemView.findViewById<ImageView>(R.id.type_checkin)
                    .setImageResource(R.drawable.stripclub)
                "cafe" -> itemView.findViewById<ImageView>(R.id.type_checkin)
                    .setImageResource(R.drawable.cafe)
                else -> itemView.findViewById<ImageView>(R.id.type_checkin)
                    .setImageResource(R.drawable.uknown)
            }

            itemView.setOnClickListener {
                events?.onClickCheckIn(item)
                Log.d("layout position",layoutPosition.toString())
                Log.d("adapter position",adapterPosition.toString())
                Log.d("position",position.toString())

            }
        }
    }

    override fun onBindViewHolder(holder: BarCheckInItemViewHolder, position: Int) {
        holder.bind(items[position],events)
    }
}