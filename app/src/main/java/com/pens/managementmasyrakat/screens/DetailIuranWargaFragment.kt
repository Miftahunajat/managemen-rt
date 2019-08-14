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
import com.pens.managementmasyrakat.adapter.AdminIuranSampahPertahunAdapter
import com.pens.managementmasyrakat.adapter.AdminIuranSosialPertahunAdapter
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import com.pens.managementmasyrakat.network.model.IuranPerTahunResponse
import com.pens.managementmasyrakat.screens.AdminIuranFragment.Companion.TYPE_SOSIAL
import com.pens.managementmasyrakat.showmessage
import com.pens.managementmasyrakat.toRupiahs
import kotlinx.android.synthetic.main.fragment_detail_iuran_warga.view.*

/**
 * A simple [Fragment] subclass.
 *
 */
class DetailIuranWargaFragment : Fragment(){

    var userKKid: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_iuran_warga, container, false)
        val detailIuranWargaFragmentArgs by navArgs<DetailIuranWargaFragmentArgs>()
        view.group_choices.setOnCheckedChangeListener { group, checkedId ->
            val circularToggle: CircularToggle = group.findViewById(checkedId)
            refreshList(circularToggle.text.toString(), detailIuranWargaFragmentArgs)
        }
        userKKid = detailIuranWargaFragmentArgs.userid
        getKKUser(detailIuranWargaFragmentArgs.userid)
        view.rv_detail_iuran_warga.layoutManager = LinearLayoutManager(context)
        view.rv_detail_iuran_warga.setHasFixedSize(true)
        view.rv_detail_iuran_warga.isNestedScrollingEnabled = false
        getHargaIuran(view)




        // Inflate the layout for this fragment
        return view
    }

    private fun getHargaIuran(view: View?) {
        val detailIuranWargaFragmentArgs by navArgs<DetailIuranWargaFragmentArgs>()
        Repository.getHargaIuranByCode(detailIuranWargaFragmentArgs.codeiuran).observe(this, Observer {
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


    private fun refreshList(
        tahun: String,
        detailIuranWargaFragmentArgs: DetailIuranWargaFragmentArgs
    ) {
        Repository.getAllIuranTahunIni(userKKid!!, tahun)
            .observe(this, Observer {
                when(it?.status){
                    Resource.LOADING ->{
                        Log.i("Loggin", it.status.toString())
                    }
                    Resource.SUCCESS ->{
                        view?.tv_title?.text = it.data?.nama
                        if (detailIuranWargaFragmentArgs.type == TYPE_SOSIAL){
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

    private fun getKKUser(id: Int) {
        Repository.getUserDetail(id).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    view?.tv_title?.text = it.data?.nama
                    userKKid = it.data?.user_kk_id
                }
                Resource.ERROR ->{
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
    }
}
