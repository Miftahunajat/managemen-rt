package com.pens.managementmasyrakat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pens.managementmasyrakat.R
import com.pens.managementmasyrakat.databinding.ItemDataWargaBinding
import com.pens.managementmasyrakat.network.model.UserResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_data_warga.view.*
import java.util.*

class UserAdapter(val onClickListener: OnClickListener) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var data: List<UserResponse> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_data_warga, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            onClickListener.onClick(position)
        }
    }

    fun swapData(data: List<UserResponse>) {
        this.data = data
        notifyDataSetChanged()
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: UserResponse) = with(itemView) {
            itemView.tv_nama.text = item.nama
            itemView.tv_nomor.text = item.no_hp
            Picasso.get().load(item.image_url.thumbnail.url).into(itemView.iv_profile)
        }
    }

    interface OnClickListener{
        fun onClick(position: Int)
    }


}