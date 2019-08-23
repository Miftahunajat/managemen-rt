package com.pens.managementmasyrakat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class BaseAdapter<T>(
    val layout: Int,
    private val clickListeners: List<(View, T) -> Unit>?
) : RecyclerView.Adapter<BaseAdapter.BaseViewModel>() {

    private var data: List<T> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewModel {
        return BaseViewModel(
            LayoutInflater.from(parent.context)
                .inflate(layout, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BaseViewModel, position: Int) {
        val item = data[position]
        clickListeners?.let {
            for (clickListener in clickListeners) clickListener(holder.itemView, item)
        }

    }

    fun swapData(data: List<T>) {
        this.data = data
        notifyDataSetChanged()
    }

    class BaseViewModel(itemView: View) : RecyclerView.ViewHolder(itemView)
}