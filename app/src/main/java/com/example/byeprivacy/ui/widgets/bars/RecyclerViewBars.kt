package com.example.byeprivacy.ui.widgets.bars

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.byeprivacy.R
import com.example.byeprivacy.data.db.models.BarDbItem
import com.example.byeprivacy.ui.fragments.BarsFragmentDirections

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
                this@RecyclerViewBars.findNavController().navigate(
                    BarsFragmentDirections.actionGlobalDetailFragment(bar.id))
                Log.d("recycler_bar",bar.id)
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
    Log.d("bars from binding", (adapter as AdapterBars).items.toString())
}