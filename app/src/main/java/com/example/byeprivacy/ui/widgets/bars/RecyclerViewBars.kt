package com.example.byeprivacy.ui.widgets.bars

import android.content.Context
import android.util.AttributeSet
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.byeprivacy.R
import com.example.byeprivacy.data.db.models.BarDbItem

class RecyclerViewBars : RecyclerView {
    private lateinit var adapterBars: AdapterBars

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        adapterBars = AdapterBars(object : InterfaceBars {
            override fun onBarClick(bar: BarDbItem) {
                this@RecyclerViewBars.findNavController().navigate(R.id.action_global_barsFragment)
            }
        })
        adapter = adapterBars
    }

}
@BindingAdapter(value = ["barItems"])
fun RecyclerViewBars.applyItems(
    bars: List<BarDbItem>?
) {
    (adapter as AdapterBars).items = bars ?: emptyList()
}