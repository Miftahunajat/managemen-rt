package com.pens.managementmasyrakat.screens


import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pens.managementmasyrakat.*
import com.pens.managementmasyrakat.databinding.FragmentDataDiriBinding
import com.pens.managementmasyrakat.network.Repository
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
            dialogView.et_data.setText(fragmentDataDiriBinding.tvNamaLengkap.text)
            dialogView.et_data.inputType= InputType.TYPE_CLASS_TEXT
            bottomSheetDialog.show()
            dialogView.tv_simpan.setOnClickListener {
                Repository.updateUser(user!!.id, nama = dialogView.et_data.text.toString()).observe(this, Observer {
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
            dialogView.et_data.setText("")
            dialogView.et_data.inputType= InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            bottomSheetDialog.show()
            dialogView.tv_simpan.setOnClickListener {
                Repository.updateUser(user!!.id, password = dialogView.et_data.text.toString()).observe(this, Observer {
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
            dialogView.et_data.setText(fragmentDataDiriBinding.tvNomorTelefon.text)
            dialogView.et_data.inputType= InputType.TYPE_CLASS_TEXT
            bottomSheetDialog.show()
            dialogView.setOnClickListener {
                Repository.updateUser(user!!.id, no_hp = dialogView.et_data.text.toString()).observe(this, Observer {
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
            dialogView.et_data.setText(fragmentDataDiriBinding.tvAlamat.text)
            dialogView.et_data.inputType= InputType.TYPE_CLASS_TEXT
            bottomSheetDialog.show()
            dialogView.setOnClickListener {
                Repository.updateUser(user!!.id, alamat = dialogView.et_data.text.toString()).observe(this, Observer {
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

        return fragmentDataDiriBinding.root
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
