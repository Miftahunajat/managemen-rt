package com.pens.managementmasyrakat.screens


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.nex3z.togglebuttongroup.button.CircularToggle
import com.pens.managementmasyrakat.R
import com.pens.managementmasyrakat.adapter.AdminIuranSampahPertahunAdapter
import com.pens.managementmasyrakat.adapter.AdminIuranSosialPertahunAdapter
import com.pens.managementmasyrakat.getNamaTahun
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import com.pens.managementmasyrakat.network.model.IuranPerTahunResponse
import com.pens.managementmasyrakat.screens.AdminIuranFragment.Companion.TYPE_SOSIAL
import com.pens.managementmasyrakat.showmessage
import kotlinx.android.synthetic.main.fragment_detail_iuran_warga.view.*
import kotlinx.android.synthetic.main.fragment_iuran.view.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class DetailIuranWarga : Fragment(), AdminIuranSosialPertahunAdapter.OnSosialClickListener,
    AdminIuranSampahPertahunAdapter.OnSampahClickListener {
    override fun onSampahClick(position: Int) {

    }

    override fun onSosialClick(position: Int) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_iuran_warga, container, false)
        view.rv_detail_iuran_warga.layoutManager = LinearLayoutManager(context)
        view.rv_detail_iuran_warga.setHasFixedSize(true)
        view.rv_detail_iuran_warga.isNestedScrollingEnabled = false
        view.group_choices.setOnCheckedChangeListener { group, checkedId ->
            val circularToggle: CircularToggle = group.findViewById(checkedId)
            context?.showmessage(circularToggle.text.toString())
            refreshList(circularToggle.text.toString())
        }



        // Inflate the layout for this fragment
        return view
    }

    private fun setupSampahAdapter(data: IuranPerTahunResponse?) {
        val adminIuranSampahPertahunAdapter = AdminIuranSampahPertahunAdapter(this)
        adminIuranSampahPertahunAdapter.swapData(data!!.iuran_per_tahun)
        view?.rv_detail_iuran_warga?.adapter = adminIuranSampahPertahunAdapter
    }

    private fun setupSosialAdapter(data: IuranPerTahunResponse?) {
        val adminIuranSosialPertahunAdapter = AdminIuranSosialPertahunAdapter(this)
        adminIuranSosialPertahunAdapter.swapData(data!!.iuran_per_tahun)
        view?.rv_detail_iuran_warga?.adapter = adminIuranSosialPertahunAdapter
    }


    private fun refreshList(tahun: String) {
        val detailIuranWargaArgs by navArgs<DetailIuranWargaArgs>()
        Repository.getAllIuranTahunIni(detailIuranWargaArgs.userid, tahun)
            .observe(this, androidx.lifecycle.Observer {
                when(it?.status){
                    Resource.LOADING ->{
                        Log.i("Loggin", it.status.toString())
                    }
                    Resource.SUCCESS ->{
                        view?.tv_nama?.text = it.data?.nama
                        if (detailIuranWargaArgs.type == TYPE_SOSIAL){
                            setupSosialAdapter(it.data)
                        }else{
                            setupSampahAdapter(it.data)
                        }
                        Log.d("@@@", it.data!!.toString())
                    }
                    Resource.ERROR ->{
                        context?.showmessage("Tidak Terhubung Di Internet")
                        Log.i("Error", it.message!!)
                    }
                }
            })
    }
}
