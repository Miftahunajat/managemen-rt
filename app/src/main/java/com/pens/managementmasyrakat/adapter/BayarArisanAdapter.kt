package com.pens.managementmasyrakat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pens.managementmasyrakat.R
import com.pens.managementmasyrakat.network.model.UserResponse
import java.util.*

class BayarArisanAdapter(val onClickListener: OnClickListener, val type: Int) : RecyclerView.Adapter<BayarArisanAdapter.BayarArisanViewHolder>() {

    private var data: List<UserResponse> = ArrayList()
    companion object {
        const val TYPE_VERIFIKASI = 1
        const val TYPE_BELUM_MEMBAYAR = 2
        const val TYPE_SUDAH_MEMBAYAR = 3
        const val TYPE_SUDAH_TARIK = 4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BayarArisanViewHolder {
        val layout = when(type){
            TYPE_VERIFIKASI -> R.layout.item_belum_terverifikasi
            TYPE_BELUM_MEMBAYAR -> R.layout.item_belum_membayar
            TYPE_SUDAH_MEMBAYAR -> R.layout.item_sudah_membayar
            else -> R.layout.item_sudah_ditarik
        }
        return BayarArisanViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(layout, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: BayarArisanViewHolder, position: Int) {
        holder.bind(data[position])
        if (type == TYPE_BELUM_MEMBAYAR) holder.itemView.setOnLongClickListener {
            onClickListener.onBelumBayarLongClick(data[position].nama, data[position].id.toString()
            )
            true
        }
        holder.itemView.setOnClickListener {
            if (type == TYPE_VERIFIKASI) onClickListener.onVerifikasiClick(data[position].id)
            else onClickListener.onClick(data[position].id)
        }
    }

    fun swapData(data: List<UserResponse>) {
        this.data = data
        notifyDataSetChanged()
    }

    class BayarArisanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: UserResponse) = with(itemView) {
            itemView.findViewById<TextView>(R.id.tv_title).text = item.nama
            itemView.findViewById<TextView>(R.id.tv_nomor).text = item.no_hp
        }
    }

    interface OnClickListener {
        fun onClick(user_id: Int)

        fun onVerifikasiClick(user_id: Int)

        fun onBelumBayarLongClick(nama: String, user_id: String)
    }
}