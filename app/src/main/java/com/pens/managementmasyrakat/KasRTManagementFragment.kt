package com.pens.managementmasyrakat


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_kas_rtmanagement.view.*
import android.util.Log
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.pens.managementmasyrakat.adapter.KasRTRAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import com.nex3z.togglebuttongroup.button.CircularToggle
import com.pens.managementmasyrakat.adapter.KasRTRAdapter.VIEW_TYPE_HEADER
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import com.pens.managementmasyrakat.network.model.DataKasRTResponse
import com.pens.managementmasyrakat.network.model.Pengeluaran


/**
 * A simple [Fragment] subclass.
 *
 */
class KasRTManagementFragment : Fragment(), KasRTRAdapter.OnBulanClickListener {

    val kasRtAdapter by lazy {
        KasRTRAdapter<Map<DataKasRTResponse, List<Pengeluaran>>>(HashMap(), this)
    }
    var tahun = ""
    var isAdmin = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_kas_rtmanagement, container, false)
        val kasRTManagementFragmentArgs by navArgs<KasRTManagementFragmentArgs>()
        isAdmin = kasRTManagementFragmentArgs.isadmin
        setupRecyclerView(view)
        view.group_choices.setOnCheckedChangeListener { group, checkedId ->
            val circularToggle: CircularToggle = group.findViewById<CircularToggle>(checkedId)
            tahun = circularToggle.text.toString()
            context?.showmessage(circularToggle.text.toString())
            refreshList(tahun)
        }
        view.tv_title.text = if (isAdmin) "Atur Kas RT" else "Kas RT"

        return view
    }

    private fun refreshList(tahun: String) {
        Repository.getPengeluaranPertahun(tahun)
            .observe(this, Observer {
                when(it?.status){
                    Resource.LOADING ->{
                        Log.d("Loading", it.status.toString())
                    }
                    Resource.SUCCESS ->{
                        val map: Map<DataKasRTResponse, List<Pengeluaran>> = toHashmap(it.data!!)
                        kasRtAdapter.swapData(map)
                    }
                    Resource.ERROR ->{
                        Log.d("Error", it.message!!)
                        context?.showmessage("Something is wrong")
                    }
                }
            })
    }

    private fun toHashmap(data: List<DataKasRTResponse>): Map<DataKasRTResponse, List<Pengeluaran>> {
        return data.map { it to it.list_pengeluarans }.toMap()
    }

    private fun setupRecyclerView(view: View?) {
        view!!.el_detail_iuran_warga.layoutManager = LinearLayoutManager(context)
        view.el_detail_iuran_warga.isNestedScrollingEnabled = false
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        view.el_detail_iuran_warga.addItemDecoration(dividerItemDecoration)

        view.el_detail_iuran_warga.adapter = kasRtAdapter
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder
            ): Boolean {return false}

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                if (kasRtAdapter.getItemViewType(position) == VIEW_TYPE_HEADER){
                    kasRtAdapter.notifyItemChanged(position)
                    return
                }else{
                    context?.showAlertDialog("Menghapus Pengeluaran","Apakah anda yakin ingin menghapus pengeluaran RT ",
                        "Data terhapus","",{ kasRtAdapter.notifyItemChanged(viewHolder.adapterPosition) }){
                        val listPengeluaran: Pengeluaran = kasRtAdapter.getItem(viewHolder.adapterPosition) as Pengeluaran
                        Repository.deletePengeluaran(listPengeluaran.id).observe(this@KasRTManagementFragment, Observer {
                            when(it?.status){
                                Resource.LOADING ->{
                                    Log.d("Loading", it.status.toString())
                                }
                                Resource.SUCCESS ->{
                                    context?.showmessage("Data terhapus")
                                    refreshList(tahun)
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
        })
        if (isAdmin) itemTouchHelper.attachToRecyclerView(view.el_detail_iuran_warga)
    }

    override fun onClick(position: Int, namaBUlan: String) {
        if (!isAdmin)
            return
        context?.showAddPengeluaranBottomSheetDialog(){keterangan, jumlah ->
            Repository.postPengeluaran(tahun, keterangan, namaBUlan, jumlah).observe(this, Observer {
                when(it?.status){
                    Resource.LOADING ->{
                        Log.d("Loading", it.status.toString())
                    }
                    Resource.SUCCESS ->{
                        context?.showmessage("Data kirim sukses")
                        refreshList(tahun)
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
