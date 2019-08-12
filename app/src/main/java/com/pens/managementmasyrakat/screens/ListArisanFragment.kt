package com.pens.managementmasyrakat.screens


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pens.managementmasyrakat.R
import com.pens.managementmasyrakat.adapter.ArisanAdapter
import com.pens.managementmasyrakat.adapter.ArisanWargaAdapter
import com.pens.managementmasyrakat.getUser
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import com.pens.managementmasyrakat.network.model.Arisan
import com.pens.managementmasyrakat.showmessage
import kotlinx.android.synthetic.main.fragment_admin_arisan.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ListArisan : Fragment() {

    var listArisan = listOf<Arisan>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_arisan, container, false)
        val user = context?.getUser()!!
        Repository.getAllArisansWithStatusIKutUser(user.id.toString(),user.jenis_kelamin_id).observe(this, Observer {
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
        val arisanAdapter = ArisanWargaAdapter(this, context?.getUser()!!.id)
        view?.rv_gelombang_arisan?.adapter = arisanAdapter
        arisanAdapter.swapData(data!!.sortedByDescending { it.user_ikut })
    }
}
