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
import com.pens.managementmasyrakat.*

import com.pens.managementmasyrakat.adapter.DataUserBayarArisanAdapter
import com.pens.managementmasyrakat.extension.finishLoading
import com.pens.managementmasyrakat.extension.showmessage
import com.pens.managementmasyrakat.extension.toLoading
import com.pens.managementmasyrakat.extension.toRupiahs
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
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
            val circularToggle: CircularToggle = group.findViewById(checkedId)
            context?.showmessage(circularToggle.text.toString())
            refreshList(circularToggle.text.toString())
        }

        return view
    }

    private fun setupUser(view: View?) {
        val dataarisanwargaDetailArgs by navArgs<DataArisanWargaDetailArgs>()
        Repository.getArisansUser(dataarisanwargaDetailArgs.idarisansuser).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    view?.rv_data_arisan?.toLoading()
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    view?.rv_data_arisan?.finishLoading()
                    view!!.tv_title.text = it.data!!.nama_peserta
                    view.tv_harga.text = it.data!!.arisan.iuran.toRupiahs()
                    view.tv_ditarik.text = if (it.data!!.tarik) "Sudah Ditarik" else "Belum Ditarik"
                    Log.d("Success", it.data.toString())
                }
                Resource.ERROR ->{
                    view?.rv_data_arisan?.finishLoading()
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
    }

    private fun refreshList(tahun: String) {
        val dataarisanwargaDetailArgs by navArgs<DataArisanWargaDetailArgs>()
        Repository.getDetailUserStatus(dataarisanwargaDetailArgs.idarisansuser, tahun)
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
