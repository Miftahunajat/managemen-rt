package com.pens.managementmasyrakat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pens.managementmasyrakat.R
import com.pens.managementmasyrakat.extension.formatToDate
import com.pens.managementmasyrakat.network.model.Arisan
import com.pens.managementmasyrakat.extension.toRupiahs
import kotlinx.android.synthetic.main.item_gelombang_arisan.view.*
import java.util.*

class ArisanAdapter(val onClickListener: OnClickListener) : RecyclerView.Adapter<ArisanAdapter.ArisanViewHolder>() {

    private var data: List<Arisan> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArisanViewHolder {
        return ArisanViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gelombang_arisan, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ArisanViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            onClickListener.onClick(position)
        }
    }

    fun swapData(data: List<Arisan>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ArisanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Arisan) = with(itemView) {
            itemView.tv_title.text = item.nama
            itemView.tv_jumlah.text = item.peserta.toString()
            itemView.tv_minimum_iuran.text = item.iuran.toRupiahs()
            itemView.tv_tanggal.text = item.selesai.formatToDate("yyyy-MM-dd","dd MMMM yyyy")
        }
    }

    interface OnClickListener {
        fun onClick(position: Int)
    }
}
