package com.pens.managementmasyrakat.network.model

import com.pens.managementmasyrakat.network.lib.DataResponse

data class DataKasRTResponse(
    val bulan: Bulan,
    val bulan_id: Int,
    val created_at: String,
    val id: Int,
    val list_pengeluarans: List<Pengeluaran>,
    val tahun: String,
    val total: String,
    val updated_at: String
) : DataResponse<DataKasRTResponse> {
    override fun retrieveData(): DataKasRTResponse = this
}