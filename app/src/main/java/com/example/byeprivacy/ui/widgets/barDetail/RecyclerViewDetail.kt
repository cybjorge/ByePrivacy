package com.example.byeprivacy.ui.widgets.barDetail

import android.content.Context
import android.util.AttributeSet
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewDetail : RecyclerView {
    private lateinit var adapterDetail: AdapterDetail

    constructor(context: Context) : super(context){
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        adapterDetail = AdapterDetail()
        adapter = adapterDetail
    }
}
@BindingAdapter(value = ["detail_items"])
fun RecyclerViewDetail.applyDetailItems(
    details: List<BarItemDetail>?
) {
    (adapter as AdapterDetail).items = details ?: emptyList()
}