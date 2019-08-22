package com.pens.managementmasyrakat.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.pens.managementmasyrakat.*
import com.pens.managementmasyrakat.extension.*
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import com.pens.managementmasyrakat.network.model.Arisan
import kotlinx.android.synthetic.main.item_gelombang_arisan_warga.view.*
import kotlinx.android.synthetic.main.item_gelombang_arisan_warga.view.tv_minimum_iuran
import kotlinx.android.synthetic.main.item_gelombang_arisan_warga.view.tv_title
import kotlinx.android.synthetic.main.item_gelombang_arisan_warga.view.tv_tanggal

class ArisanWargaAdapterX(val fragment: Fragment, val user_id: Int) : RecyclerView.Adapter<ArisanWargaAdapterX.ArisanViewHolder>() {

    private var data: List<Arisan> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArisanViewHolder {
        return ArisanViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gelombang_arisan_warga, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ArisanViewHolder, position: Int) {
        val item = data[position]
        holder.setIsRecyclable(false)
        holder.bind(data[position])
        if (item.user_ikut != null && item.user_ikut) {
            holder.itemView.tv_daftar.visibility = View.GONE
//            holder.itemView.tv_lihat_detail.setOnClickListener {
//                fragment.findNavController().navigate(ListArisanDirections.actionListArisanToDataArisanWargaDetail(item.id, user_id.toString()))
//            }
        } else {
//            holder.itemView.tv_lihat_detail.visibility = View.GONE
            holder.itemView.tv_daftar.addDialogDaftarArisanOnClick(item.nama) {
                val userId = fragment.context?.getUser()!!.id
                Repository.postDaftarArisan(item.id, userId.toString()).observe(fragment, androidx.lifecycle.Observer {
                    when(it?.status){
                        Resource.LOADING ->{
                            Log.d("Loading", it.status.toString())
                        }
                        Resource.SUCCESS ->{
                            fragment.context?.showmessage("Pendaftaran Dikirim")
                        }
                        Resource.ERROR ->{
                            Log.d("Error", it.message!!)
                            fragment.context?.showmessage("Pendaftaran Gagal")
                        }
                    }
                })
            }
        }
    }

    fun swapData(data: List<Arisan>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ArisanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Arisan) = with(itemView) {
            itemView.tv_title.text = item.nama
            itemView.tv_minimum_iuran.text = item.iuran.toRupiahs()
            itemView.tv_tanggal.text = item.selesai.formatToDate("yyyy-MM-dd","dd MMMM yyyy")
        }
    }
}
