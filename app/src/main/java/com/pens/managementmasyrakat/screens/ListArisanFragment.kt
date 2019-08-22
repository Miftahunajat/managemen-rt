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
import com.pens.managementmasyrakat.adapter.ArisanWargaAdapter
import com.pens.managementmasyrakat.adapter.ArisanWargaAdapterX
import com.pens.managementmasyrakat.adapter.KasRTRAdapter
import com.pens.managementmasyrakat.extension.getUser
import com.pens.managementmasyrakat.extension.showAlertDialog
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import com.pens.managementmasyrakat.network.model.Arisan
import com.pens.managementmasyrakat.extension.showmessage
import com.pens.managementmasyrakat.network.model.AllUserArisanResponse
import com.pens.managementmasyrakat.network.model.DataKasRTResponse
import com.pens.managementmasyrakat.network.model.Pengeluaran
import kotlinx.android.synthetic.main.fragment_admin_arisan.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ListArisanFragment : Fragment(), ArisanWargaAdapter.OnClickListener {
    override fun onChildClick(arisanUserId: Int) {
        findNavController().navigate(ListArisanFragmentDirections.actionListArisanToDataArisanWargaDetail(arisanUserId))
    }

    override fun onHeaderClick(id: Int) {
        context?.showAlertDialog(title = "Pendaftaran arisan",message = "Apakah anda yakin ingin mendaftar arisan", onYes = "Daftar Berhasil") {
            Repository.postDaftarArisan(id, context?.getUser()?.id.toString()).observe(this, Observer {
                when(it?.status){
                    Resource.LOADING ->{
                        Log.d("Loading", it.status.toString())
                    }
                    Resource.SUCCESS ->{
                        refreshRecyclerView()
                        context?.showmessage("Pendaftaran Dikirim")
                    }
                    Resource.ERROR ->{
                        Log.d("Error", it.message!!)
                        context?.showmessage("Pendaftaran Gagal")
                    }
                }
            })
        }
    }

    val user_id by lazy {
        Repository.getUser(context!!)?.id
    }
    val arisanWargaAdapter by lazy {
        ArisanWargaAdapter<Map<Arisan, List<AllUserArisanResponse>>>(HashMap(), this)
    }
    var listArisan = listOf<Arisan>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_arisan, container, false)
        refreshRecyclerView()
        // Inflate the layout for this fragment
        return view
    }

    private fun setupAdapter(data: List<Arisan>?) {

        view?.rv_gelombang_arisan?.layoutManager = LinearLayoutManager(context)
        view?.rv_gelombang_arisan?.adapter = arisanWargaAdapter
        arisanWargaAdapter.swapData(toHashmap(data!!))
    }

    private fun toHashmap(data: List<Arisan>): Map<Arisan, List<AllUserArisanResponse>> {
        return data.map { it -> it to it.arisans_users.filter { it.user_id == user_id }}.toMap()
    }

    private fun refreshRecyclerView(){
        val user = context?.getUser()!!
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
    }
}
