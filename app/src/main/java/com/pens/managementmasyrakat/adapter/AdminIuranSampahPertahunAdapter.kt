package com.pens.managementmasyrakat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pens.managementmasyrakat.R
import com.pens.managementmasyrakat.network.model.IuranPerTahun
import kotlinx.android.synthetic.main.item_data_bayar.view.*
import java.util.*

class AdminIuranSampahPertahunAdapter(val onClickListener: OnSampahClickListener) : RecyclerView.Adapter<AdminIuranSampahPertahunAdapter.IuranPetahunViewHolder>() {

    private var data: List<IuranPerTahun> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IuranPetahunViewHolder {
        return IuranPetahunViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_data_bayar, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: IuranPetahunViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            onClickListener.onSampahClick(position)
        }
    }

    fun swapData(data: List<IuranPerTahun>) {
        this.data = data
        notifyDataSetChanged()
    }

    class IuranPetahunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: IuranPerTahun) = with(itemView) {
            itemView.tv_nama_bulan.text = item.bulan
            itemView.checkBox.isChecked = item.iuran_sampah
        }
    }

    interface OnSampahClickListener {
        fun onSampahClick(position: Int)
    }
}