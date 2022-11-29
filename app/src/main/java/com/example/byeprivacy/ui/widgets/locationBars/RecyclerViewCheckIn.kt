package com.example.byeprivacy.ui.widgets.locationBars

import android.content.Context
import android.util.AttributeSet
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.byeprivacy.data.db.models.BarApiItem


class RecyclerViewCheckIn : RecyclerView {
    private lateinit var adapterBarsCheckIn: AdapterBarsCheckIn
    var events: InterfaceBarsCheckIn?=null
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        adapterBarsCheckIn = AdapterBarsCheckIn(object : InterfaceBarsCheckIn {
            override fun onClickCheckIn(bar: BarApiItem) {
                events?.onClickCheckIn(bar)
            }
        })
        adapter = adapterBarsCheckIn
    }

}
@BindingAdapter(value = ["barItems"])
fun RecyclerViewCheckIn.applyItems(
    bars: List<BarApiItem>?
) {
    (adapter as AdapterBarsCheckIn).items = bars ?: emptyList()
}