package com.pens.managementmasyrakat.network.model

import com.pens.managementmasyrakat.network.lib.DataResponse

data class HargaIuranResponse(
    val code: Int,
    val created_at: String,
    val harga: String,
    val id: Int,
    val nama_iuran: String,
    val updated_at: String
) : DataResponse<HargaIuranResponse> {
    override fun retrieveData(): HargaIuranResponse = this
}