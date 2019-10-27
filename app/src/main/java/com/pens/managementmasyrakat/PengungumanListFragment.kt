package com.pens.managementmasyrakat


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pens.managementmasyrakat.adapter.PengungumanAdapter
import com.pens.managementmasyrakat.extension.*
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import com.pens.managementmasyrakat.network.model.PengungumanResponse
import kotlinx.android.synthetic.main.fragment_pengunguman_list.view.*
import kotlinx.android.synthetic.main.item_pengunguman.view.*
import java.util.ArrayList


class PengungumanListFragment : Fragment(), PengungumanAdapter.OnClickListener {

    var listPengunguman: List<PengungumanResponse> = ArrayList()

    override fun onClick(position: Int) {
        findNavController().navigate(PengungumanListFragmentDirections.actionPengungumanListFragmentToPengungumanFragment(listPengunguman[position].id))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pengunguman_list, container, false)
        Repository.getAllPengunguman().observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    view.rv_pengunguman.toLoading()
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    view.rv_pengunguman.finishLoading()
                    setupPengunguman(view, it.data)
                    Log.d("Success", it.data.toString())
                }
                Resource.ERROR ->{
                    view.rv_pengunguman.finishLoading()
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
        // Inflate the layout for this fragment
        return view
    }

    private fun setupPengunguman(view: View?, data: List<PengungumanResponse>?) {
        view?.rv_pengunguman?.setupNoAdapter(R.layout.item_pengunguman, data!!,::bindData)
    }

    private fun bindData(view: View, pengungumanResponse: PengungumanResponse){
        view.tv_title.text = pengungumanResponse.title
    }


}
