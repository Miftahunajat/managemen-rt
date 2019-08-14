package com.pens.managementmasyrakat.screens


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.nex3z.togglebuttongroup.button.CircularToggle

import com.pens.managementmasyrakat.R
import com.pens.managementmasyrakat.adapter.DataUserBayarArisanAdapter
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import com.pens.managementmasyrakat.showmessage
import com.pens.managementmasyrakat.toRupiahs
import kotlinx.android.synthetic.main.fragment_data_arisan_warga_detail.view.*
import kotlinx.android.synthetic.main.fragment_data_arisan_warga_detail.view.group_choices
import kotlinx.android.synthetic.main.fragment_data_arisan_warga_detail.view.tv_title


/**
 * A simple [Fragment] subclass.
 *
 */
class DataArisanWargaDetail : Fragment() {

    val userBayarArisanAdapter = DataUserBayarArisanAdapter(this)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_data_arisan_warga_detail, container, false)
        setupUser(view)
        view.rv_data_arisan.adapter = userBayarArisanAdapter
        view.rv_data_arisan.layoutManager = LinearLayoutManager(context)
        view.rv_data_arisan.setHasFixedSize(true)
        view.rv_data_arisan.isNestedScrollingEnabled = false
        view.group_choices.setOnCheckedChangeListener { group, checkedId ->
            val circularToggle: CircularToggle = group.findViewById<CircularToggle>(checkedId)
            context?.showmessage(circularToggle.text.toString())
            refreshList(circularToggle.text.toString())
        }

        return view
    }

    private fun setupUser(view: View?) {
        val dataarisanwargaDetailArgs by navArgs<DataArisanWargaDetailArgs>()
        Repository.getArisansUser(dataarisanwargaDetailArgs.idarisan,dataarisanwargaDetailArgs.iduser).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    view!!.tv_title.text = it.data!!.user.nama
                    view.tv_harga.text = it.data!!.arisan.iuran.toRupiahs()
                    view.tv_ditarik.text = if (it.data!!.tarik) "Sudah Ditarik" else "Belum Ditarik"
                    Log.d("Success", it.data.toString())
                }
                Resource.ERROR ->{
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
    }

    private fun refreshList(tahun: String) {
        val dataarisanwargaDetailArgs by navArgs<DataArisanWargaDetailArgs>()
        Repository.getDetailUserStatus(dataarisanwargaDetailArgs.idarisan, tahun,dataarisanwargaDetailArgs.iduser.toString())
            .observe(this, Observer {
                when(it?.status){
                    Resource.LOADING ->{
                        Log.i("Loggin", it.status.toString())
                    }
                    Resource.SUCCESS ->{
                        userBayarArisanAdapter.swapData(it.data!!)
                        Log.d("@@@", it.data!!.toString())
                    }
                    Resource.ERROR ->{
                        context?.showmessage("Data kosong")
                        Log.i("Error", it.message!!)
                    }
                }
            })
    }
}
