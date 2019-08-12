package com.pens.managementmasyrakat.screens


import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.pens.managementmasyrakat.adapter.UserAdapter
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import com.pens.managementmasyrakat.network.model.UserResponse
import com.pens.managementmasyrakat.showmessage
import com.pens.managementmasyrakat.toRupiahs
import androidx.core.text.HtmlCompat
import com.pens.managementmasyrakat.showAlertDialog
import com.pens.managementmasyrakat.showEditableBottomSheetDialog
import kotlinx.android.synthetic.main.fragment_search_warga.view.*


// TODO: Rename parameter arguments, choose names that match

/**
 * A simple [Fragment] subclass.
 *
 */
class SearchWargaFragment : Fragment(), UserAdapter.OnClickListener {

    var listWarga = listOf<UserResponse>()
    var hargaRupiah = ""
    var type_id = 1
    var code = 1

    override fun onClick(position: Int) {
        val searchWargaFragmentArgs by navArgs<SearchWargaFragmentArgs>()
        findNavController().navigate(SearchWargaFragmentDirections.actionSearchWargaFragment2ToDetailIuranWarga(
            searchWargaFragmentArgs.type, listWarga[position].id, code
        ))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(com.pens.managementmasyrakat.R.layout.fragment_search_warga, container, false)
        getALLKKUser()
        getHargaIuranJumlah()
        view.tv_harga_iuran.setOnClickListener { addDialogToHargaIuran() }
        // Inflate the layout for this fragment
        return view
    }

    private fun addDialogToHargaIuran() {
        context?.showAlertDialog(title = "Apakah anda ingin merubah data harga iuran",onYes = "") {
            context?.showEditableBottomSheetDialog("Masukkan data iuran baru", hargaRupiah, InputType.TYPE_CLASS_NUMBER) {
                Repository.updateHargaIuran(type_id, it).observe(this, Observer {
                    when(it?.status){
                        Resource.LOADING ->{
                            Log.d("Loading", it.status.toString())
                        }
                        Resource.SUCCESS ->{
                            getHargaIuranJumlah()
                            context?.showmessage("Iuran sudah di update")
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
    }

    private fun getHargaIuranJumlah() {
        val searchWargaFragmentArgs by navArgs<SearchWargaFragmentArgs>()
        Repository.getHargaIuranByCode(searchWargaFragmentArgs.type).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    val type = it.data!!.nama_iuran
                    hargaRupiah = it.data!!.harga
                    val harga = it.data!!.harga.toRupiahs()
                    type_id = it.data!!.id
                    code = it.data!!.code
                    val sourceString = "Harga Iuran $type : <b>$harga</b> / Bulan"

                    view?.tv_harga_iuran?.text = HtmlCompat.fromHtml(sourceString, HtmlCompat.FROM_HTML_MODE_LEGACY)
                    Log.d("Success", it.data.toString())
                }
                Resource.ERROR ->{
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
    }

    private fun getALLKKUser() {
        Repository.getAllKKUser().observe(this, Observer {
            when (it?.status) {
                Resource.LOADING -> {
                    Log.i("Loggin", it.status.toString())
                }
                Resource.SUCCESS -> {
                    listWarga = it.data!!
                    setupAdapter(it.data)
                    Log.d("@@@", it.data!!.toString())
                }
                Resource.ERROR -> {
                    context?.showmessage("Nama / Password salah")
                    Log.i("Error", it.message!!)
                }
            }
        })
    }

    private fun setupAdapter(data: List<UserResponse>?) {
        val userAdapter = UserAdapter(this)
        userAdapter.swapData(data!!)
        view?.rootView!!.rv_item_warga.layoutManager = LinearLayoutManager(context)
        view?.rootView!!.rv_item_warga.adapter = userAdapter
        view?.et_carinama!!.addTextChangedListener(userAdapter)
    }
}
