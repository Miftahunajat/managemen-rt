package com.pens.managementmasyrakat.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pens.managementmasyrakat.R
import com.pens.managementmasyrakat.databinding.PengungumanFragmentBinding
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import com.pens.managementmasyrakat.extension.showmessage


class PengungumanFragment : Fragment() {

    lateinit var pengungumanFragmentBinding: PengungumanFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.pengunguman_fragment, container, false)
        pengungumanFragmentBinding = DataBindingUtil.inflate<PengungumanFragmentBinding>(inflater, R.layout.pengunguman_fragment, container, false)

        getFirstPengunguman(view)
        pengungumanFragmentBinding.tvMenuAwal.setOnClickListener {
            findNavController().navigate(PengungumanFragmentDirections.actionPengungumanFragmentToHomeFragment())
        }
        return pengungumanFragmentBinding.root
    }

    private fun getFirstPengunguman(view: View?) {
        val pengungumanFragmentArgs by navArgs<PengungumanFragmentArgs>()
        Repository.getAllPengunguman().observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    val pengungumanResponse = it.data!!.filter { it.id == pengungumanFragmentArgs.idpengunguman }.first()
                    pengungumanFragmentBinding.data = pengungumanResponse
                    Log.d("Success", it.data!!.last().toString())
                }
                Resource.ERROR ->{
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
    }
}