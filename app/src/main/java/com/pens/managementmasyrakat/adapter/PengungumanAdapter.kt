package com.pens.managementmasyrakat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pens.managementmasyrakat.R
import com.pens.managementmasyrakat.network.model.PengungumanResponse
import kotlinx.android.synthetic.main.item_pengunguman.view.*
import java.util.*

class PengungumanAdapter(val onClickListener: OnClickListener) : RecyclerView.Adapter<PengungumanAdapter.PengungumanViewHolder>() {

    private var data: List<PengungumanResponse> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PengungumanViewHolder {
        return PengungumanViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pengunguman, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: PengungumanViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.tv_title.setOnClickListener {
            onClickListener.onClick(position)
        }
    }

    fun swapData(data: List<PengungumanResponse>) {
        this.data = data
        notifyDataSetChanged()
    }

    class PengungumanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: PengungumanResponse) = with(itemView) {
            itemView.tv_title.text = item.title
        }
    }

    interface OnClickListener {
        fun onClick(position: Int)
    }
}