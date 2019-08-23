package com.pens.managementmasyrakat.extension

import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pens.managementmasyrakat.adapter.BaseAdapter

//fun RecyclerView.setupWithAdapter(
//    listItem: List<Any> = ArrayList()
//){
//    this.layoutManager = LinearLayoutManager(context)
//    val adapter = BaseAdapter<?>()
//
//    }
//}

fun RecyclerView.addDecoration() {
    val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
    this.addItemDecoration(dividerItemDecoration)
}