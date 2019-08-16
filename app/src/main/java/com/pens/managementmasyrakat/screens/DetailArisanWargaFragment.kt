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
import com.pens.managementmasyrakat.adapter.UserBayarArisanAdapter
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import kotlinx.android.synthetic.main.fragment_detail_arisan_warga.view.*


/**
 * A simple [Fragment] subclass.
 *
 */
class DetailArisanWargaFragment : Fragment(){

    val userBayarArisanAdapter = UserBayarArisanAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail_arisan_warga, container, false)
        setupUser(view)
        view.cb_ditarik.addEventDialogListener {
            val detailArisanFragmentArgs by navArgs<DetailArisanWargaFragmentArgs>()
            Repository.postTarikArisan(detailArisanFragmentArgs.idarisan, detailArisanFragmentArgs.iduser.toString()).observe(
                this, Observer {
                    when(it?.status){
                        Resource.LOADING ->{
                            Log.d("Loading", it.status.toString())
                        }
                        Resource.SUCCESS ->{
                            context?.showmessage("Update Berhasil")
                            Log.d("Success", it.data.toString())
                        }
                        Resource.ERROR ->{
                            Log.d("Error", it.message!!)
                            context?.showmessage("Something is wrong")
                        }
                    }
                }
            )
        }
        view.rv_detail_arisan_warga.layoutManager = LinearLayoutManager(context)
        view.rv_detail_arisan_warga.setHasFixedSize(true)
        view.rv_detail_arisan_warga.isNestedScrollingEnabled = false
        view.rv_detail_arisan_warga.adapter = userBayarArisanAdapter
        view.group_choices.setOnCheckedChangeListener { group, checkedId ->
            val circularToggle: CircularToggle = group.findViewById<CircularToggle>(checkedId)
            context?.showmessage(circularToggle.text.toString())
            refreshList(circularToggle.text.toString())
        }

        // Inflate the layout for this fragment
        return view
    }

    private fun setupUser(view: View?) {
        val detailArisanFragmentArgs by navArgs<DetailArisanWargaFragmentArgs>()
        Repository.getArisansUser(detailArisanFragmentArgs.idarisan,detailArisanFragmentArgs.iduser.toString()).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    view?.rv_detail_arisan_warga?.toLoading()
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    view?.rv_detail_arisan_warga?.finishLoading()
                    view!!.tv_title.text = it.data!!.user.nama
                    view.tv_harga.text = it.data!!.arisan.iuran.toRupiahs()
                    view.cb_ditarik.isChecked = it.data!!.tarik
                    Log.d("Success", it.data.toString())
                }
                Resource.ERROR ->{
                    view?.rv_detail_arisan_warga?.finishLoading()
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
    }


    private fun refreshList(tahun: String) {
        val detailArisanFragmentArgs by navArgs<DetailArisanWargaFragmentArgs>()
        Repository.getDetailUserStatus(detailArisanFragmentArgs.idarisan, tahun,detailArisanFragmentArgs.iduser.toString())
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
