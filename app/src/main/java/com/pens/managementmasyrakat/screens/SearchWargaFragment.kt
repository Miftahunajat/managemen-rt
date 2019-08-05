package com.pens.managementmasyrakat.screens


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.pens.managementmasyrakat.R
import com.pens.managementmasyrakat.adapter.UserAdapter
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import com.pens.managementmasyrakat.network.model.UserResponse
import com.pens.managementmasyrakat.saveUser
import com.pens.managementmasyrakat.screens.AdminIuranFragment.Companion.TYPE_SOSIAL
import com.pens.managementmasyrakat.showmessage
import kotlinx.android.synthetic.main.fragment_search_warga.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SearchWargaFragment : Fragment(), UserAdapter.OnClickListener {

    var listWarga = listOf<UserResponse>()

    override fun onClick(position: Int) {
        val searchWargaFragmentArgs by navArgs<SearchWargaFragmentArgs>()
        findNavController().navigate(SearchWargaFragmentDirections.actionSearchWargaFragment2ToDetailIuranWarga(
            searchWargaFragmentArgs.type, listWarga[position].user_kk_id
        ))
        context?.showmessage(listWarga[position].nama)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_warga, container, false)
        Repository.getAllUser().observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.i("Loggin", it.status.toString())
                }
                Resource.SUCCESS ->{
                    listWarga= it.data!!
                    setupAdapter(it.data)
                    Log.d("@@@", it.data!!.toString())
                }
                Resource.ERROR ->{
                    context?.showmessage("Nama / Password salah")
                    Log.i("Error", it.message!!)
                }
            }
        })
        // Inflate the layout for this fragment
        return view
    }

    private fun setupAdapter(data: List<UserResponse>?) {
        val userAdapter = UserAdapter(this)
        userAdapter.swapData(data!!)
        view?.rootView!!.rv_item_warga.layoutManager = LinearLayoutManager(context)
        view?.rootView!!.rv_item_warga.adapter = userAdapter
    }


}
