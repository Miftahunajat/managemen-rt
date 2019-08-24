package com.pens.managementmasyrakat.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pens.managementmasyrakat.R
import com.pens.managementmasyrakat.network.model.UserResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_data_warga.view.*
import java.util.*

class UserAdapter(val onClickListener: OnClickListener) : RecyclerView.Adapter<UserAdapter.UserViewHolder>(),
TextWatcher{
    private var baseData: List<UserResponse> = ArrayList()
    private var currData: List<UserResponse> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_data_warga, parent, false)
        )
    }

    override fun getItemCount() = currData.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(currData[position])
        holder.itemView.setOnClickListener {
            onClickListener.onClick(position)
        }
    }

    fun swapData(baseData: List<UserResponse>) {
        this.baseData = baseData
        this.currData = baseData
        notifyDataSetChanged()
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: UserResponse) = with(itemView) {
            itemView.tv_title.text = item.nama
            itemView.tv_alamat.text = item.alamat
            Picasso.get().load(item.image_url.thumbnail.url).error(R.drawable.img_avatar).placeholder(R.drawable.img_avatar).into(itemView.iv_profile)
        }
    }

    interface OnClickListener{
        fun onClick(position: Int)
    }

    override fun afterTextChanged(p0: Editable?) {}
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        currData = baseData.filter { it.nama.contains(p0!!, ignoreCase = true) || p0.isBlank() }
        notifyDataSetChanged()
    }
}