package com.pens.managementmasyrakat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.pens.managementmasyrakat.R
import com.pens.managementmasyrakat.network.model.UserBayarArisan
import com.pens.managementmasyrakat.toInt
import kotlinx.android.synthetic.main.item_data_terbayar.view.*

class DataUserBayarArisanAdapter(val fragment: Fragment) :
    RecyclerView.Adapter<DataUserBayarArisanAdapter.UserBayarArisanViewbHolder>() {

    private var data: List<UserBayarArisan> = ArrayList()
    companion object {
        val textColor = arrayOf(R.color.red_500, R.color.green_500)
        val stringsTitle = arrayOf("Belum membayar", "Sudah membayar")
        val drawableCircle = arrayOf(R.drawable.background_red_line_soft, R.drawable.background_green_line_soft)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserBayarArisanViewbHolder {
        return UserBayarArisanViewbHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_data_terbayar, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: UserBayarArisanViewbHolder, position: Int) {
        holder.bind(data[position])
    }

    fun swapData(data: List<UserBayarArisan>) {
        this.data = data
        notifyDataSetChanged()
    }

    class UserBayarArisanViewbHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: UserBayarArisan) = with(itemView) {
            itemView.tv_bulan.text = item.bulan.nama_bulan
            var index: Int = item.bayar.toInt()
            itemView.tv_status.text = stringsTitle[index]
            itemView.tv_status.setTextColor(ContextCompat.getColor(context, DataIuranAdapter.textColor[index]))
            itemView.tv_status.background = ContextCompat.getDrawable(context,drawableCircle[index])
        }
    }
}