package com.pens.managementmasyrakat.screens


import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.pens.managementmasyrakat.R
import com.pens.managementmasyrakat.getUser
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import com.pens.managementmasyrakat.showmessage
import kotlinx.android.synthetic.main.fragment_tambah_arisan.*
import kotlinx.android.synthetic.main.fragment_tambah_arisan.view.*
import java.text.DateFormatSymbols
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TambahArisanFragment : Fragment() {

    var tanggalSelesai = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tambah_arisan, container, false)
        val user = context?.getUser()
        val calendar = Calendar.getInstance()
        view.et_content_desc.setOnClickListener {
            DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { _, year, month, day ->
                tanggalSelesai = "$year-$month-$day"
                view.et_content_desc.setText("$day ${DateFormatSymbols().months[month-1]} $year")
            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE)).show()
        }
        view.tv_tambahkan.setOnClickListener {
            if (et_body.text.isEmpty() || et_title.text.isEmpty() || et_content_desc.text.isEmpty()){
                context?.showmessage("Tidak boleh ada yang kosong")
                return@setOnClickListener
            }
            Repository.postArisan(user!!.jenis_kelamin_id, tanggalSelesai, view.et_body.text.toString(), view.et_body.text.toString())
                .observe(this, androidx.lifecycle.Observer {
                    when(it?.status){
                        Resource.LOADING ->{
                            Log.d("Loading", it.status.toString())
                        }
                        Resource.SUCCESS ->{
                            findNavController().navigate(TambahArisanFragmentDirections.actionTambahArisanFragmentToAdminArisanFragment())
                            Log.d("Success", it.data.toString())
                        }
                        Resource.ERROR ->{
                            Log.d("Error", it.message!!)
                            context?.showmessage("Something is wrong")
                        }
                    }
                })
        }
        // Inflate the layout for this fragment
        return view
    }


}
