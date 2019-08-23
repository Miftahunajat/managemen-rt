package com.pens.managementmasyrakat.extension

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pens.managementmasyrakat.R
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.model.UserResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_bottom_sheet_dialog.view.et_data
import kotlinx.android.synthetic.main.fragment_bottom_sheet_dialog.view.tv_batal
import kotlinx.android.synthetic.main.fragment_bottom_sheet_dialog.view.tv_simpan
import kotlinx.android.synthetic.main.fragment_bottom_sheet_dialog.view.tv_title
import kotlinx.android.synthetic.main.fragment_tambah_list_pengeluaran.view.*
import java.text.SimpleDateFormat
import java.util.*

fun Context.showmessage(msg: String){
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.getUser(): UserResponse?{
    return Repository.getUser(this)
}

//fun Context.getKkId(): Int?{
//    return Repository.getKkId(this)
//}

fun Context.saveUser(user: UserResponse){
    Repository.saveUser(user, this)
}

fun UserResponse.isAdminArisan(): Boolean?{
    return this.roles.any { roles -> roles.code == 1 }
}

fun UserResponse.isAdminIuran(): Boolean?{
    return this.roles.any { roles -> roles.code == 2 }
}

fun UserResponse.isPengurusRT(): Boolean?{
    return this.roles.any { roles -> roles.code == 3 }
}

fun Boolean.toInt() = if (this) 1 else 0

fun Calendar.getNamaTahun(): String{
    val yearDate = SimpleDateFormat("yyyy", Locale.getDefault())
    val year_name = yearDate.format(this.getTime())
    return year_name
}

fun Calendar.getNamaBulan(): String{
    val month_date = SimpleDateFormat("MMMM", Locale.getDefault())
    val month_name = month_date.format(this.getTime())
    return month_name

}

fun CheckBox.addEventDialogListener(update: (CheckBox) -> Unit){
    this.setOnClickListener {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("Perubahan data")
        builder.setMessage("Apakah anda yakin ingin merubah data")
        builder.setPositiveButton("YES"){ _, _ ->
            update(this)
        }

        builder.setNegativeButton("No"){ _, _ ->
            this.context.showmessage("Anda membatalkan")
            this.isChecked = !this.isChecked
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}

fun String.toRupiahs(): String{
    val money = String.format("%,d",this.toFloat().toInt())
    return "Rp, $money"
}

fun String.formatToDate(patternBefore: String, patternAfter: String): String {
    var spf = SimpleDateFormat(patternBefore, Locale.getDefault())
    val newDate = spf.parse(this)
    spf = SimpleDateFormat(patternAfter, Locale.getDefault())
    return spf.format(newDate!!)
}

fun Calendar.getFormattedTanggal(): String{
    val tanggal = this[Calendar.DATE]
    val bulan = this.getNamaBulan()
    val tahun = this[Calendar.YEAR]
    return "$tanggal $bulan $tahun"
}

fun View.addDialogDaftarArisanOnClick(judulArisan: String, update: () -> Unit){
    this.setOnClickListener {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("Mendaftar Arisan")
        builder.setMessage("Apakah anda yakin ingin mendaftar $judulArisan")
        builder.setPositiveButton("YES"){ _, _ ->
            this.context.showmessage("Permintaan Dikirim")
            update()
        }

        builder.setNegativeButton("No"){ _, _ ->
            this.context.showmessage("Permintaan Dibatalkan")
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}

fun Context.showEditableBottomSheetDialog(title: String = "Masukkan data baru",
                                          text: String,
                                          inputType: Int = InputType.TYPE_CLASS_TEXT,
                                          update: (String) -> Unit){
    val bottomSheetDialog = BottomSheetDialog(this)
    val dialogView = LayoutInflater.from(this).inflate(R.layout.fragment_bottom_sheet_dialog, null)
    dialogView.tv_batal.setOnClickListener {
        bottomSheetDialog.dismiss()
    }
    bottomSheetDialog.setContentView(dialogView)
    dialogView.tv_title.text = title
    dialogView.et_data.setText(text)
    dialogView.et_data.inputType= inputType
    dialogView.tv_simpan.setOnClickListener {
        update(dialogView.et_data.text.toString())
        bottomSheetDialog.dismiss()
    }
    bottomSheetDialog.show()
}

fun Context.showAlertDialog(
    title: String = "Perubahan Data",
    message: String = "Apakah anda yakin ingin melalukannya",
    onYes: String = "Perubahan Data Berhasil",
    onNo: String = "Perubahan Data Gagal",
    onNegatifPressed: () -> Unit = {},
    update: () -> Unit
){
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton("YES"){ _, _ ->
        this.showmessage(onYes)
        update()
    }

    builder.setNegativeButton("No"){ _, _ ->
        onNegatifPressed()
        this.showmessage(onNo)
    }
    val dialog: AlertDialog = builder.create()
    dialog.show()
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    Picasso.get().load(url).into(view)
}

fun Uri.getRealPath(context: Context): String? {
    var cursor: Cursor? = null
    try{
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        cursor = context.contentResolver.query(this, proj, null, null, null);
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return cursor?.getString(columnIndex!!)
    } finally {
        cursor?.close()
    }


}

fun Context.showAddPengeluaranBottomSheetDialog(title: String = "Masukkan data baru", inputType: Int = InputType.TYPE_CLASS_TEXT, update: (String, String) -> Unit){
    val bottomSheetDialog = BottomSheetDialog(this)
    val dialogView = LayoutInflater.from(this).inflate(R.layout.fragment_tambah_list_pengeluaran, null)
    dialogView.tv_batal.setOnClickListener {
        bottomSheetDialog.dismiss()
    }
    bottomSheetDialog.setContentView(dialogView)
    dialogView.tv_title.text = title
    dialogView.et_data.inputType= inputType
    dialogView.tv_simpan.setOnClickListener {
        update(dialogView.et_data.text.toString(),dialogView.et_data2.text.toString())
        bottomSheetDialog.dismiss()
    }
    bottomSheetDialog.window!!.attributes.gravity = Gravity.BOTTOM
    bottomSheetDialog.show()
}


fun View.toLoading() {
    if (this.tag == null){
        val progressBar = ProgressBar(this.context)
        this.tag = progressBar
        progressBar.isIndeterminate = true
        progressBar.visibility = View.VISIBLE
        val currentLayoutParams = this.layoutParams as ConstraintLayout.LayoutParams
        this.visibility = View.INVISIBLE
        (this.parent as ConstraintLayout).addView(progressBar, currentLayoutParams)
    }
}

fun View.finishLoading(){
    if (this.tag != null && this.tag is ProgressBar){
        val progressBar = this.tag as ProgressBar
        progressBar.visibility = View.GONE
        this.tag = null
        this.visibility = View.VISIBLE
    }
}