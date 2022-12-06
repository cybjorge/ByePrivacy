package com.example.byeprivacy.ui.widgets.barDetail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
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

        @SuppressLint("SetTextI18n", "CutPasteId")
        fun bind(item: BarItemDetail) {
            when (item.key) {
                "amenity" -> {
                    itemView.findViewById<TextView>(R.id.name).text = item.key

                    when (item.value) {
                        "pub" -> itemView.findViewById<ImageView>(R.id.value_image)
                            .setImageResource(R.drawable.pub)
                        "bar" -> itemView.findViewById<ImageView>(R.id.value_image)
                            .setImageResource(R.drawable.baar)
                        "fast_food" -> itemView.findViewById<ImageView>(R.id.value_image)
                            .setImageResource(R.drawable.fastfood)
                        "nightclub" -> itemView.findViewById<ImageView>(R.id.value_image)
                            .setImageResource(R.drawable.restaurant)
                        "restaurant" -> itemView.findViewById<ImageView>(R.id.value_image)
                            .setImageResource(R.drawable.restaurant)
                        "stripclub" -> itemView.findViewById<ImageView>(R.id.value_image)
                            .setImageResource(R.drawable.stripclub)
                        "cafe" -> itemView.findViewById<ImageView>(R.id.value_image)
                            .setImageResource(R.drawable.cafe)
                        else -> itemView.findViewById<ImageView>(R.id.value_image)
                            .setImageResource(R.drawable.uknown)
                    }
                }
                "name"->
                {
                    //TODO get rid of name
                }
                "website"->{
                    itemView.findViewById<TextView>(R.id.name).visibility=View.INVISIBLE
                    itemView.findViewById<TextView>(R.id.value).visibility=View.INVISIBLE
                    itemView.findViewById<Button>(R.id.detail_web).visibility=View.VISIBLE
                    itemView.findViewById<Button>(R.id.detail_web).setOnClickListener {
                        val intent= Intent(Intent.ACTION_VIEW, Uri.parse(item.value.toString()))
                        itemView.context.startActivity(intent)
                    }
                }

                else -> {
                    itemView.findViewById<TextView>(R.id.name).text = item.key
                    itemView.findViewById<TextView>(R.id.value).text = item.value
                }

            }
        }
    }
}