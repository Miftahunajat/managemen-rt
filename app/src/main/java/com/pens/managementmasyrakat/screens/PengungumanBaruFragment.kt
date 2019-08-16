package com.pens.managementmasyrakat.screens


import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.fragment_pengunguman_baru.view.*
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.net.Uri
import android.util.Log
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.pens.managementmasyrakat.*
import com.pens.managementmasyrakat.service.NotificationService
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_tambah_arisan.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import java.io.File
import okhttp3.RequestBody




/**
 * A simple [Fragment] subclass.
 *
 */
class PengungumanBaruFragment : Fragment() {

    private var mCropImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pengunguman_baru, container, false)
        view.iv_content.setOnClickListener { onSelectImageClick(view.iv_content) }
        view.tv_umumkan_warga.setOnClickListener { postPengunguman(view) }
        // Inflate the layout for this fragment
        return view
    }

    private fun postPengunguman(view: View) {
        val file: File?
        var image: MultipartBody.Part? = null
        if (mCropImageUri == null ) file = null
        else {
            file = File(mCropImageUri?.getRealPath(context!!)!!)
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file)
            image = MultipartBody.Part.createFormData("file", file.name, requestFile)
        }
        val requestTitle = MultipartBody.Part.createFormData("title",et_title.text.toString())
        val requestBody= MultipartBody.Part.createFormData("body",et_body.text.toString())
        val requestContentDesc = MultipartBody.Part.createFormData("content_desc",et_content_desc.text.toString())


        Repository.postPengungumanPhoto(
            requestTitle,
            requestBody,
            image,
            requestContentDesc
        ).observe(this, Observer {
            when(it?.status){
                Resource.LOADING ->{
                    view.tv_umumkan_warga.toLoading()
                    Log.d("Loading", it.status.toString())
                }
                Resource.SUCCESS ->{
                    view.tv_umumkan_warga.finishLoading()
                    NotificationService.getInstance(context).sendNotifToWargas(
                        et_title.text.toString(),
                        et_body.text.toString()
                    )
                    findNavController().navigate(PengungumanBaruFragmentDirections.actionPengungumanBaruFragmentToPengurusRTFragment())
                    Log.d("Success", it.data.toString())
                }
                Resource.ERROR ->{
                    view.tv_umumkan_warga.finishLoading()
                    Log.d("Error", it.message!!)
                    context?.showmessage("Something is wrong")
                }
            }
        })
    }

    @SuppressLint("NewApi")
    fun onSelectImageClick(view: View) {
        if (CropImage.isExplicitCameraPermissionRequired(context!!)) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE)
        } else {
            CropImage.startPickImageActivity(context!!,this)
        }
    }

    @SuppressLint("NewApi")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageUri = CropImage.getPickImageResultUri(context!!, data)
            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(context!!, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri
                context?.showmessage("Permission dibutuhkan untuk mengambil gambar")
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE
                )
            } else {
                // no permissions required or already granted, can start crop image activity
                view?.iv_content!!.setImageURI(imageUri)
                mCropImageUri = imageUri
            }
        }
    }
}
