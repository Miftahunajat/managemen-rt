package com.pens.managementmasyrakat.network.model

import com.pens.managementmasyrakat.network.lib.DataResponse

data class AllUserArisanResponse(
    val arisan_id: Int,
    val created_at: String,
    val daftar: Boolean,
    val id: Int,
    val ikut: Boolean,
    val nama_peserta: String,
    val sudah_membayar: Boolean,
    val tarik: Boolean,
    val updated_at: String,
    val user_id: Int,
    val user: UserResponse,
    val arisan: Arisan
) : DataResponse<AllUserArisanResponse> {
    override fun retrieveData(): AllUserArisanResponse = this
}