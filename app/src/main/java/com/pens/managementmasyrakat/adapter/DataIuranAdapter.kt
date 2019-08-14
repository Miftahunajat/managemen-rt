package com.pens.managementmasyrakat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pens.managementmasyrakat.R
import com.pens.managementmasyrakat.network.model.IuranPerTahun
import com.pens.managementmasyrakat.toInt
import kotlinx.android.synthetic.main.item_data_terbayar.view.*
import java.util.*

class DataIuranAdapter(val type: Int) : RecyclerView.Adapter<DataIuranAdapter.DataIuranViewHolder>() {

    private var data: List<IuranPerTahun> = ArrayList()
    companion object {
        const val TYPE_SOSIAL = 1
        const val TYPE_SAMPAH = 2
        val textColor = arrayOf(R.color.red_500, R.color.green_500)
        val stringsTitle = arrayOf("Belum membayar", "Sudah membayar")
        val drawableCircle = arrayOf(R.drawable.background_red_line_soft, R.drawable.background_green_line_soft)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataIuranViewHolder {
        return DataIuranViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_data_terbayar, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: DataIuranViewHolder, position: Int) {
        holder.bind(data[position], type)
        holder.itemView.setOnClickListener {
        }
    }

    fun swapData(data: List<IuranPerTahun>) {
        this.data = data
        notifyDataSetChanged()
    }

    class DataIuranViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: IuranPerTahun, type: Int) = with(itemView) {
            itemView.tv_bulan.text = item.bulan
            var index: Int
            if (type == TYPE_SOSIAL)
                index = item.iuran_sosial.toInt()
            else
                index = item.iuran_sampah.toInt()
            itemView.tv_status.setTextColor(ContextCompat.getColor(context, textColor[index]))
            itemView.tv_status.text = stringsTitle[index]
            itemView.tv_status.background = ContextCompat.getDrawable(context,drawableCircle[index])
        }
    }


}