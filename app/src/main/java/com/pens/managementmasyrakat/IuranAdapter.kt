package com.pens.managementmasyrakat

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_iuran.view.*
import java.util.*

class IuranAdapter : RecyclerView.Adapter<IuranAdapter.IuranViewHolder>() {

    private var data: List<Iuran> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IuranViewHolder {
        return IuranViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_iuran, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: IuranViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: List<Iuran>) {
        this.data = data
        notifyDataSetChanged()
    }

    class IuranViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Iuran) = with(itemView) {
            itemView.tv_nama.text = item.name
            itemView.imageView.setImageDrawable(ContextCompat.getDrawable(context, item.imageId))
            ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(ContextCompat.getColor(context, item.colorId)))
            // TODO: Bind the data with View
            setOnClickListener {
                // TODO: Handle on click
            }
        }
    }
}