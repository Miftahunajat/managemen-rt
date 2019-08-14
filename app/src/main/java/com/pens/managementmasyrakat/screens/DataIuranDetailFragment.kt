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
import com.pens.managementmasyrakat.adapter.DataIuranAdapter
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import kotlinx.android.synthetic.main.fragment_data_iuran_detail.view.*
import kotlinx.android.synthetic.main.fragment_detail_iuran_warga.view.group_choices


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 *
 */
class DataIuranDetailFragment : Fragment() {

    lateinit var dataIuranAdapter: DataIuranAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_data_iuran_detail, container, false)
        val dataIuranDetailFragmentArgs by navArgs<DataIuranDetailFragmentArgs>()
        view.rv_data_iuran.layoutManager = LinearLayoutManager(context)
        dataIuranAdapter = DataIuranAdapter(dataIuranDetailFragmentArgs.type)
        view.rv_data_iuran.adapter = dataIuranAdapter
        view.rv_data_iuran.setHasFixedSize(true)
        view.rv_data_iuran.isNestedScrollingEnabled = false
        view.group_choices.setOnCheckedChangeListener { group, checkedId ->
            val circularToggle: CircularToggle = group.findViewById(checkedId)
            context?.showmessage(circularToggle.text.toString())
            refreshList(circularToggle.text.toString())
        }
        getHargaIuran(view)

        // Inflate the layout for this fragment
        return view
    }

    private fun refreshList(tahun: String) {
        val dataIuranDetailFragmentArgs by navArgs<DataIuranDetailFragmentArgs>()
        Repository.getAllIuranTahunIni(dataIuranDetailFragmentArgs.userkkid, tahun)
            .observe(this, Observer {
                when(it?.status){
                    Resource.LOADING ->{
                        view!!.rv_data_iuran.toLoading()
                        Log.i("Loggin", it.status.toString())
                    }
                    Resource.SUCCESS ->{
                        view!!.rv_data_iuran.finishLoading()
                        view?.tv_title?.text = it.data?.nama
                        dataIuranAdapter.swapData(it.data!!.iuran_per_tahun)
                        Log.d("@@@", it.data!!.toString())
                    }
                    Resource.ERROR ->{
                        view!!.rv_data_iuran.finishLoading()
                        context?.showmessage("Tidak Terhubung Di Internet")
                        Log.i("Error", it.message!!)
                    }
                }
            })
    }

    private fun getHargaIuran(view: View?) {
        val dataIuranDetailFragmentArgs by navArgs<DataIuranDetailFragmentArgs>()
        Repository.getHargaIuranByCode(dataIuranDetailFragmentArgs.codeiuran).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{

                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    view!!.tv_harga.text = it.data!!.harga.toRupiahs()
                    Log.d("Success", it.data.toString())
                }
                Resource.ERROR ->{

                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
    }
}
