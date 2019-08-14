package com.pens.managementmasyrakat.screens


import android.content.Context
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pens.managementmasyrakat.*
import com.pens.managementmasyrakat.databinding.FragmentDataDiriBinding
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.Repository.getKeluargas
import com.pens.managementmasyrakat.network.lib.Resource
import com.pens.managementmasyrakat.network.model.UserResponse
import kotlinx.android.synthetic.main.fragment_bottom_sheet_dialog.view.*
import kotlinx.android.synthetic.main.fragment_data_diri.view.*



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
/**
 * A simple [Fragment] subclass.
 *
 */
class DataDiriFragment : Fragment() {

    var user: UserResponse? = null
    lateinit var fragmentDataDiriBinding: FragmentDataDiriBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentDataDiriBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_data_diri,
            container, false
        )
        user = context?.getUser()
        refreshUser()

        val bottomSheetDialog = BottomSheetDialog(context!!)
        val dialogView = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, null)
        dialogView.tv_batal.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        fragmentDataDiriBinding.lifecycleOwner = this

        bottomSheetDialog.setContentView(dialogView)
        fragmentDataDiriBinding.icEditNama.setOnClickListener {
            context?.showEditableBottomSheetDialog(text = fragmentDataDiriBinding.tvNamaLengkap.text.toString()){
                Repository.updateUser(user!!.id, nama = it).observe(this, Observer {
                    when(it?.status){
                        Resource.LOADING ->{
                            Log.d("Loading", it.status.toString())
                        }
                        Resource.SUCCESS ->{
                            refreshUser()
                            bottomSheetDialog.dismiss()
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
        fragmentDataDiriBinding.icEditPassword.setOnClickListener {
            context?.showEditableBottomSheetDialog(text = "", inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD){
                Repository.updateUser(user!!.id, password = it).observe(this, Observer {
                    when(it?.status){
                        Resource.LOADING ->{
                            Log.d("Loading", it.status.toString())
                        }
                        Resource.SUCCESS ->{
                            refreshUser()
                            bottomSheetDialog.dismiss()
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
        fragmentDataDiriBinding.icEditNomor.setOnClickListener {
            context?.showEditableBottomSheetDialog(text = fragmentDataDiriBinding.tvNomorTelefon.text.toString()){
                Repository.updateUser(user!!.id, no_hp = it).observe(this, Observer {
                    when(it?.status){
                        Resource.LOADING ->{
                            Log.d("Loading", it.status.toString())
                        }
                        Resource.SUCCESS ->{
                            refreshUser()
                            bottomSheetDialog.dismiss()
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
        fragmentDataDiriBinding.icEditAlamat.setOnClickListener {
            context?.showEditableBottomSheetDialog(text = fragmentDataDiriBinding.tvAlamat.text.toString()) {
                Repository.updateUser(user!!.id, alamat = it).observe(this, Observer {
                    when(it?.status){
                        Resource.LOADING ->{
                            Log.d("Loading", it.status.toString())
                        }
                        Resource.SUCCESS ->{
                            refreshUser()
                            bottomSheetDialog.dismiss()
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
        getKeluarFromUser(view, user!!.id)
        return fragmentDataDiriBinding.root
    }

    private fun getKeluarFromUser(view: View?, id: Int) {
        Repository.getKeluargas(id).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    val values: Array<String> = it.data!!.map { it.nama }.toTypedArray()
                    val adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1, values)
                    view!!.rv_keluarga.adapter = adapter
                    Log.d("Success", values.toString())
                }
                Resource.ERROR ->{
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
    }

    fun refreshUser(){
        Repository.getUserDetail(user!!.id).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    user = it.data!!
                    context?.saveUser(user!!)
                    fragmentDataDiriBinding.user = user
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
