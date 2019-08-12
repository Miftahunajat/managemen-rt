package com.pens.managementmasyrakat.screens


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pens.managementmasyrakat.R
import com.pens.managementmasyrakat.adapter.ArisanAdapter
import com.pens.managementmasyrakat.getUser
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import com.pens.managementmasyrakat.network.model.Arisan
import com.pens.managementmasyrakat.showmessage
import kotlinx.android.synthetic.main.fragment_admin_arisan.view.*

/**
 * A simple [Fragment] subclass.
 *
 */
class AdminArisanFragment : Fragment(), ArisanAdapter.OnClickListener {

    var listArisan = listOf<Arisan>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_arisan, container, false)
        val user = context?.getUser()!!
        view.tv_tambah_gelombang.setOnClickListener {
            it.findNavController().navigate(AdminArisanFragmentDirections.actionAdminArisanFragmentToTambahArisanFragment())
        }
        Repository.getAllArisan(user.jenis_kelamin_id).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    listArisan = it.data!!
                    setupAdapter(it.data)
                    Log.d("Success", it.data.toString())

                }
                Resource.ERROR ->{
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
        // Inflate the layout for this fragment
        return view
    }

    private fun setupAdapter(data: List<Arisan>?) {
        view?.rv_gelombang_arisan?.layoutManager = LinearLayoutManager(context)
        val arisanAdapter = ArisanAdapter(this)
        view?.rv_gelombang_arisan?.adapter = arisanAdapter
        arisanAdapter.swapData(data!!)
    }

    override fun onClick(position: Int) {
        val arisanId = listArisan[position].id
        findNavController().navigate(AdminArisanFragmentDirections.actionAdminArisanFragmentToBayarArisanFragment(arisanId))
    }
}
