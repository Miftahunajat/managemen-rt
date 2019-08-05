package com.pens.managementmasyrakat

import android.content.Context
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.pens.managementmasyrakat.network.Repository
import com.pens.managementmasyrakat.network.model.UserResponse
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

fun CheckBox.addEventDialogListener(onYes: () -> Unit){
    this.setOnClickListener {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("Perubahan data")
        builder.setMessage("Apakah anda yakin ingin merubah data")
        builder.setPositiveButton("YES"){dialog, which ->
            // Do something when user press the positive button
            Toast.makeText(this.context,"Ok, we change the app background.",Toast.LENGTH_SHORT).show()
            // Change the app background color

        }


        // Display a negative button on alert dialog
        builder.setNegativeButton("No"){dialog,which ->
            this.context.showmessage("You are not agree.")
            this.isChecked = !this.isChecked
        }
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }
}