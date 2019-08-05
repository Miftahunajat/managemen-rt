package com.pens.managementmasyrakat.network

import com.pens.managementmasyrakat.network.model.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("search/repositories")
    fun getRepos(@Query("q") query: String): Deferred<Response<ListResponse<Repo>>>

    @FormUrlEncoded
    @POST("login")
    fun login(@Field("nama") nama: String, @Field("password") password: String): Deferred<Response<UserResponse>>

    @GET("user/{kk_id}/all_iuran")
    fun getIuranBulanIni(
        @Path("kk_id") id: Int,
        @Query("bulan") namaBulan: String,
        @Query("tahun") tahun: String
    ): Deferred<Response<IuranBulanIniResponse>>

    @GET("user")
    fun getAllUser(
    ): Deferred<Response<ListResponse<UserResponse>>>

    @GET("user/{kk_id}/iuran_sosial_sampah")
    fun getIuranTahunIni(
        @Path("kk_id") id: Int,
        @Query("tahun") tahun: String
    ): Deferred<Response<IuranPerTahunResponse>>
}