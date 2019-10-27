package com.pens.managementmasyrakat.extension

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pens.managementmasyrakat.adapter.BaseAdapter

@Suppress("UNCHECKED_CAST")
fun <T> RecyclerView.setupNoAdapter(layout: Int,
                                      listItem: List<T> = ArrayList(),
                                      bindView: (View, T) -> Unit) {
    this.layoutManager = LinearLayoutManager(context)
    val adapter = BaseAdapter(layout,bindView)
    this.adapter = adapter
    (this.adapter as BaseAdapter<T>).swapData(listItem)
    this.adapter?.notifyDataSetChanged()
}

@Suppress("UNCHECKED_CAST")
fun <T> RecyclerView.refreshNoAdapterRecyclerView(listItem: List<T> = ArrayList()) {
    this.adapter?.let {
        (it as BaseAdapter<T>).swapData(listItem)
        it.notifyDataSetChanged()
    }
}

fun RecyclerView.addDecoration() {
    val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
    this.addItemDecoration(dividerItemDecoration)
}