package com.pens.managementmasyrakat.extension

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.internal.Primitives
import com.pens.managementmasyrakat.network.ManagemenApi
import com.pens.managementmasyrakat.network.model.UserResponse

fun Context.savePref(key: String, value: String){
    this.getSharedPreferences(ManagemenApi.PREFNAME, 0).edit().putString(key, value).apply()
}

fun Context.savePref(key: String, value: Int){
    this.getSharedPreferences(ManagemenApi.PREFNAME, 0).edit().putInt(key, value).apply()
}

fun Context.savePref(key: String, value: Boolean){
    this.getSharedPreferences(ManagemenApi.PREFNAME, 0).edit().putBoolean(key, value).apply()
}

fun Context.savePrefObj(key: String, value: Any){
    val json = Gson().toJson(value)
    this.getSharedPreferences(ManagemenApi.PREFNAME, 0).edit().putString(key, json).apply()
}

fun Context.getPref(key: String, value: String): String?{
    return this.getSharedPreferences(ManagemenApi.PREFNAME, 0).getString(key, null)
}

fun Context.getPref(key: String, value: Int): Int{
    return this.getSharedPreferences(ManagemenApi.PREFNAME, 0).getInt(key, 0)
}

fun Context.getPref(key: String, value: Boolean): Boolean{
    return this.getSharedPreferences(ManagemenApi.PREFNAME, 0).getBoolean(key, false)
}

fun <T> Context.getPrefObj(key: String, classType: Class<T>): T?{
    val jsonString : String? = this.getSharedPreferences(ManagemenApi.PREFNAME, 0).getString(key, null)
    return if (jsonString != null) Gson().fromJson(jsonString, classType) else null
}