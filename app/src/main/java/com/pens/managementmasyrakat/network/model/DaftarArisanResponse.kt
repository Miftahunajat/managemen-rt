package com.pens.managementmasyrakat.network.model

import com.pens.managementmasyrakat.network.lib.DataResponse

data class DaftarArisanResponse(
    val arisan_id: Int,
    val created_at: String,
    val daftar: Boolean,
    val id: Int,
    val ikut: Boolean,
    val tarik: Boolean,
    val updated_at: String,
    val user_id: Int,
    val user: UserResponse
) : DataResponse<DaftarArisanResponse> {
    override fun retrieveData(): DaftarArisanResponse = this
}