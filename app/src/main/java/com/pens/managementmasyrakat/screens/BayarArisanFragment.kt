package com.pens.managementmasyrakat.screens


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.pens.managementmasyrakat.*
import com.pens.managementmasyrakat.adapter.BayarArisanAdapter
import com.pens.managementmasyrakat.extension.*
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import kotlinx.android.synthetic.main.fragment_bayar_arisan.view.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 *
 */
class BayarArisanFragment : Fragment(), BayarArisanAdapter.OnClickListener {
    override fun onVerifikasiClick(arisans_users_id: Int) {
        context?.showAlertDialog {
            Repository.postIkutArisan(arisans_users_id).observe(this, Observer {
                when(it?.status){
                    Resource.LOADING ->{
                        Log.d("Loading", it.status.toString())
                    }
                    Resource.SUCCESS ->{
                        refreshViewRecycler(view!!)
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

    override fun onLongBelumMembayarClick(arisans_users_id: Int, nama_peserta: String) {
        context?.showAlertDialog("Sudah membayar ?", "Apakah anda ingin merubah $nama_peserta menjadi sudah membayar pada $bulan $tahun") {
            updateArisan(arisans_users_id)
        }
    }

    private fun updateArisan(arisans_user_id: Int?) {
        Repository.updateArisan(arisans_user_id!!, bulan, tahun, bayar = true).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    refreshViewRecycler(view!!)
                    Log.d("Success", it.data.toString())
                }
                Resource.ERROR ->{
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
    }

    override fun onClick(arisans_users_id: Int) {
        findNavController().navigate(BayarArisanFragmentDirections.actionBayarArisanFragmentToDetailArisanWargaFragment(
            arisans_users_id))
    }

    var arisan_id: Int? = null

    val bulan by lazy { Calendar.getInstance().getNamaBulan() }
    val tahun by lazy { Calendar.getInstance().getNamaTahun() }

    val bayarArisanAdapter = BayarArisanAdapter(this)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bayar_arisan, container, false)
        view.rv_item_warga_bayar_arisan.layoutManager = LinearLayoutManager(context)
        val bayarArisanFragmentArgs by navArgs<BayarArisanFragmentArgs>()
        val sourceString = "Data Bulan <b>${Calendar.getInstance().getNamaBulan()}</b> \nHarga Iuran : ${bayarArisanFragmentArgs.hargaiuran.toString().toRupiahs()} / Bulan"
        view.tv_harga_iuran.text = HtmlCompat.fromHtml(sourceString, HtmlCompat.FROM_HTML_MODE_LEGACY)
        view.tv_judul.text = bayarArisanFragmentArgs.namaarisan
        arisan_id = bayarArisanFragmentArgs.idarisan
        refreshViewRecycler(view)
        view.tv_tutup_arisan.setOnClickListener(::onTutupArisanClick)
        return view
    }

    private fun onTutupArisanClick(view: View) {
        context?.showAlertDialog("Menutup arisan","Apakah anda yakin ingin menutup arisan","Arisan berhasil di tutup",""){
            Repository.updateArisan(arisan_id!!, true).observe(this, Observer {
                when(it?.status){
                    Resource.LOADING ->{
                        Log.d("Loading", it.status.toString())
                    }
                    Resource.SUCCESS ->{
                        findNavController().navigate(BayarArisanFragmentDirections.actionBayarArisanFragmentToAdminArisanFragment())
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

    private fun refreshViewRecycler(view: View) {
        val bayarArisanFragmentArgs by navArgs<BayarArisanFragmentArgs>()
        Repository.getAllStatusArisan(bayarArisanFragmentArgs.idarisan).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    view.rv_item_warga_bayar_arisan.adapter = bayarArisanAdapter
                    bayarArisanAdapter.swapData(it.data!!)
                    view.et_carinama.addTextChangedListener(bayarArisanAdapter)
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