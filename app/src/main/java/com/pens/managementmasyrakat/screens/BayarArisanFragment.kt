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
import com.pens.managementmasyrakat.*
import com.pens.managementmasyrakat.adapter.BayarArisanAdapter
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import kotlinx.android.synthetic.main.fragment_bayar_arisan.view.*
import kotlinx.android.synthetic.main.inc_arisan_belum_membayar.view.*
import kotlinx.android.synthetic.main.inc_arisan_sudah_ditarik.view.*
import kotlinx.android.synthetic.main.inc_arisan_sudah_membayar.view.*
import kotlinx.android.synthetic.main.inc_arisan_verifikasi.view.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class BayarArisanFragment : Fragment(), BayarArisanAdapter.OnClickListener {
    override fun onVerifikasiClick(user_id: Int) {
        context?.showAlertDialog {
            Repository.postIkutArisan(arisan_id!!, user_id.toString()).observe(this, Observer {
                when(it?.status){
                    Resource.LOADING ->{
                        Log.d("Loading", it.status.toString())
                    }
                    Resource.SUCCESS ->{
                        refreshViewRecycler(view)
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

    override fun onClick(user_id: Int) {
        findNavController().navigate(BayarArisanFragmentDirections.actionBayarArisanFragmentToDetailArisanWargaFragment(
            arisan_id!!, user_id
            ))
    }

    var arisan_id: Int? = null

    companion object {
        const val TYPE_VERIFIKASI = 1
        const val TYPE_BELUM_MEMBAYAR = 2
        const val TYPE_SUDAH_MEMBAYAR = 3
        const val TYPE_SUDAH_TARIK = 4
    }

    val verifikasiAdapter = BayarArisanAdapter(this, TYPE_VERIFIKASI)
    val belumMembayarAdapter = BayarArisanAdapter(this, TYPE_BELUM_MEMBAYAR)
    val sudahMembayarAdapter = BayarArisanAdapter(this, TYPE_SUDAH_MEMBAYAR)
    val sudahTarikAdapter = BayarArisanAdapter(this, TYPE_SUDAH_TARIK)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bayar_arisan, container, false)

        view.inc_arisan_verifikasi.rv_belum_verifikasi.setupWithBayarArisanAdapter(verifikasiAdapter)
        view.inc_arisan_belum_membayar.rv_belum_membayar.setupWithBayarArisanAdapter(belumMembayarAdapter)
        view.inc_sudah_membayar.rv_sudah_membayar.setupWithBayarArisanAdapter(sudahMembayarAdapter)
        view.inc_sudah_ditarik.rv_sudah_ditarik.setupWithBayarArisanAdapter(sudahTarikAdapter)
        view.tv_nama_bulan.text = Calendar.getInstance().getNamaBulan()
        view.tv_tanggal.text = Calendar.getInstance().getFormattedTanggal()

        refreshViewRecycler(view)
        return view
    }

    private fun refreshViewRecycler(view: View?) {
        val bayarArisanFragmentArgs by navArgs<BayarArisanFragmentArgs>()
        Repository.getAllStatusArisan(bayarArisanFragmentArgs.idarisan).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    arisan_id = it.data!!.id
                    verifikasiAdapter.swapData(it.data!!.peserta_verifikasi)
                    belumMembayarAdapter.swapData(it.data!!.peserta_belum_membayar)
                    sudahMembayarAdapter.swapData(it.data!!.peserta_sudah_membayar)
                    sudahTarikAdapter.swapData(it.data!!.peserta_sudah_ditarik)
                    view!!.tv_nama_arisan.text = it.data!!.nama
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
