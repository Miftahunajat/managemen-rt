package com.pens.managementmasyrakat.network

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.pens.managementmasyrakat.network.ManagemenApi.PREFNAME
import com.pens.managementmasyrakat.network.lib.networkCall
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.google.gson.Gson
import com.pens.managementmasyrakat.network.model.*


object Repository {
    fun saveUser(user: UserResponse, context: Context){
        val gson = Gson()
        val json = gson.toJson(user)
        context.getSharedPreferences(PREFNAME, 0).edit().putString("user",json).apply()
//        context.getSharedPreferences(PREFNAME, 0).edit().putInt("kkid",kkid).apply()
    }

    fun getUser(context: Context): UserResponse? {
        val jsonString : String? = context.getSharedPreferences(PREFNAME, 0).getString("user",null)
        val gson = Gson()
        return if (jsonString != null) gson.fromJson(jsonString, UserResponse::class.java) else null
    }

//    fun getKkId(context: Context): Int? {
//        return context.getSharedPreferences(PREFNAME, 0).getInt("kkid",0)
//    }

    fun clearUser(context: Context) {
        context.getSharedPreferences(PREFNAME, 0).edit().clear().apply()
    }

    fun getRepos(query: String) = networkCall<ListResponse<Repo>, List<Repo>> {
        client = ManagemenApi.apiService.getRepos(query)
    }

    fun login(nama: String, password: String) = networkCall<UserResponse, UserResponse> {
        client = ManagemenApi.apiService.login(nama, password)
    }

    fun getAllIuranBulanIni(id:Int, bulan: String, tahun: String) = networkCall<IuranBulanIniResponse, IuranBulanIniResponse> {
        client = ManagemenApi.apiService.getIuranBulanIni(id,bulan, tahun)
    }

    fun getAllIuranTahunIni(id:Int, tahun: String) = networkCall<IuranPerTahunResponse, IuranPerTahunResponse> {
        client = ManagemenApi.apiService.getIuranTahunIni(id, tahun)
    }

    fun getAllUser() = networkCall<ListResponse<UserResponse>, List<UserResponse>> {
        client = ManagemenApi.apiService.getAllUser()
    }
}

object ManagemenApi {
    var interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    var API_BASE_URL: String = "http://1b69ec21.ngrok.io/"
    var httpClient = OkHttpClient.Builder().addInterceptor(interceptor)
    var builder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
    var retrofit = builder
        .client(httpClient.build())
        .build()

    var apiService = retrofit.create<ApiService>(ApiService::class.java)
    val PREFNAME = "MANAGEMEN_RT"
}
