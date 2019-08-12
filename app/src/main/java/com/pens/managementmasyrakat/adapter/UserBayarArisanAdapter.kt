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
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import com.pens.managementmasyrakat.network.model.UserBayarArisan
import com.pens.managementmasyrakat.showmessage
import kotlinx.android.synthetic.main.item_data_bayar.view.*

class UserBayarArisanAdapter(val fragment: Fragment) :
    RecyclerView.Adapter<UserBayarArisanAdapter.UserBayarArisanViewbHolder>() {

    private var data: List<UserBayarArisan> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserBayarArisanViewbHolder {
        return UserBayarArisanViewbHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_data_bayar, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: UserBayarArisanViewbHolder, position: Int) {
        val item = data[position]
        holder.itemView.tv_status.addEventDialogListener {
            Repository.updateArisan(item.arisans_user_id, item.bulan.nama_bulan, item.tahun, it.isChecked)
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

    fun swapData(data: List<UserBayarArisan>) {
        this.data = data
        notifyDataSetChanged()
    }

    class UserBayarArisanViewbHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: UserBayarArisan) = with(itemView) {
            itemView.tv_nama_bulan.text = item.bulan.nama_bulan
            itemView.tv_status.isChecked = item.bayar
        }
    }
}