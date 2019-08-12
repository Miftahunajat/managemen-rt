package com.pens.managementmasyrakat

import android.content.Context
import android.graphics.drawable.Drawable
import android.provider.SyncStateContract.Helpers.update
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pens.managementmasyrakat.adapter.BayarArisanAdapter
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.lib.Resource
import com.pens.managementmasyrakat.network.model.UserResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_bottom_sheet_dialog.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    val money = String.format("%,d",this.toInt())
    return "Rp, $money"
}

fun String.formatToDate(patternBefore: String, patternAfter: String): String {
    var spf = SimpleDateFormat(patternBefore, Locale.getDefault())
    val newDate = spf.parse(this)
    spf = SimpleDateFormat(patternAfter, Locale.getDefault())
    return spf.format(newDate!!)
}

fun RecyclerView.setupWithBayarArisanAdapter(
        adapter: BayarArisanAdapter,
        layoutManager: LinearLayoutManager = LinearLayoutManager(this.context)
    ){
    if(this.layoutManager == null) this.layoutManager = layoutManager
    this.adapter = adapter
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
        this.showmessage(onNo)
    }
    val dialog: AlertDialog = builder.create()
    dialog.show()
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    Picasso.get().load(url).into(view)
}