package com.pens.managementmasyrakat.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.pens.managementmasyrakat.R
import com.pens.managementmasyrakat.addEventDialogListener
import com.pens.managementmasyrakat.getUser
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import com.pens.managementmasyrakat.network.model.IuranPerTahun
import com.pens.managementmasyrakat.showmessage
import kotlinx.android.synthetic.main.item_data_bayar.view.*


class AdminIuranSosialPertahunAdapter(val fragment: Fragment) : RecyclerView.Adapter<AdminIuranSosialPertahunAdapter.IuranPetahunViewHolder>() {

    private var data: List<IuranPerTahun> = ArrayList()
    val user_kk_id: Int
    init {
        user_kk_id = fragment.context?.getUser()?.user_kk_id!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IuranPetahunViewHolder {
        return IuranPetahunViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_data_bayar, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: IuranPetahunViewHolder, position: Int) {
        val item = data[position]
        holder.itemView.tv_status.addEventDialogListener {
            Repository.updateIuranResponse(user_kk_id, item.bulan, item.tahun, "sosial", it.isChecked)
                .observe(fragment, Observer {
                    when(it?.status){
                        Resource.LOADING ->{
                            Log.i("Loggin", it.status.toString())
                        }
                        Resource.SUCCESS ->{
                            fragment.context?.showmessage("Update Berhasil")
                            Log.d("@@@", it.data!!.toString())
                        }
                        Resource.ERROR ->{
                            fragment.context?.showmessage("Update gagal")
                            Log.i("Error", it.message!!)
                        }
                    }
                })
        }
        holder.bind(data[position])
    }

    fun swapData(data: List<IuranPerTahun>) {
        this.data = data
        notifyDataSetChanged()
    }

    class IuranPetahunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: IuranPerTahun) = with(itemView) {
            itemView.tv_nama_bulan.text = item.bulan
            itemView.tv_status.isChecked = item.iuran_sosial
        }
    }
}