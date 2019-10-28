package com.pens.managementmasyrakat.screens


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs

import com.pens.managementmasyrakat.R
import com.pens.managementmasyrakat.databinding.FragmentDataWargaBinding
import com.pens.managementmasyrakat.extension.showmessage
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import kotlinx.android.synthetic.main.fragment_data_diri.view.*

/**
 * A simple [Fragment] subclass.
 */
class DataWargaFragment : Fragment() {

    lateinit var fragmentDataWargaBinding: FragmentDataWargaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentArgs by navArgs<DataWargaFragmentArgs>()
        fragmentDataWargaBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_data_warga,container, false)
        getDetailWarga(fragmentArgs.userid)
        // Inflate the layout for this fragment

        return fragmentDataWargaBinding.root
    }

    fun getDetailWarga(userId: String){
        Repository.getUserDetail(userId.toInt()).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    fragmentDataWargaBinding.user = it.data!!;
                    getKeluargaFromUser(view, userId.toInt())
                }
                Resource.ERROR ->{
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
    }

    private fun getKeluargaFromUser(view: View?, id: Int) {
        Repository.getKeluargas(id).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    val values: Array<String> = it.data!!.map { it.nama }.toTypedArray()
                    val adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, values)
                    view!!.rv_keluarga.adapter = adapter
                    Log.d("Success", values.toString())
                }
                Resource.ERROR ->{
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
    }
}
