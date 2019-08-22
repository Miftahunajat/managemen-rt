package com.pens.managementmasyrakat.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pens.managementmasyrakat.R
import com.pens.managementmasyrakat.extension.toBelumMembayar
import com.pens.managementmasyrakat.extension.toBelumVerifikasi
import com.pens.managementmasyrakat.extension.toSudahMembayar
import com.pens.managementmasyrakat.network.model.AllUserArisanResponse
import kotlinx.android.synthetic.main.item_data_warga_arisan.view.*
import java.util.*

class BayarArisanAdapter(val onClickListener: OnClickListener) : RecyclerView.Adapter<BayarArisanAdapter.BayarArisanViewHolder>(), TextWatcher {

    private var currData: List<AllUserArisanResponse> = ArrayList()
    private var baseData: List<AllUserArisanResponse> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BayarArisanViewHolder {
        return BayarArisanViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_data_warga_arisan, parent, false)
        )
    }

    override fun getItemCount() = currData.size

    override fun onBindViewHolder(holder: BayarArisanViewHolder, position: Int) {
        holder.bind(currData[position])
        val item = currData[position]
        if (!item.sudah_membayar)
            holder.itemView.setOnLongClickListener {
                onClickListener.onLongBelumMembayarClick(item.id, item.nama_peserta)
                true
            }
        holder.itemView.setOnClickListener {
            if (item.ikut) onClickListener.onClick(currData[position].id)
            else onClickListener.onVerifikasiClick(item.id)
        }
    }

    fun swapData(data: List<AllUserArisanResponse>) {
        this.currData= data
        this.baseData= data
        notifyDataSetChanged()
    }

    class BayarArisanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: AllUserArisanResponse) = with(itemView) {
            itemView.tv_nama.text = item.nama_peserta
            itemView.tv_alamat.text = item.user.alamat
            itemView.tv_sudah_ditarik.visibility = if (item.tarik) View.VISIBLE else View.INVISIBLE
            if (!item.ikut) itemView.tv_status_bayar.toBelumVerifikasi()
            else if (item.sudah_membayar) itemView.tv_status_bayar.toSudahMembayar()
            else itemView.tv_status_bayar.toBelumMembayar()
        }
    }

    interface OnClickListener {
        fun onClick(arisans_users_id: Int)
        fun onVerifikasiClick(arisans_users_id: Int)
        fun onLongBelumMembayarClick(arisans_users_id: Int, nama_peserta: String)
    }

    override fun afterTextChanged(p0: Editable?) {}
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        currData = baseData.filter { it.nama_peserta.contains(p0!!, ignoreCase = true) || p0.isBlank() }
        notifyDataSetChanged()
    }
}