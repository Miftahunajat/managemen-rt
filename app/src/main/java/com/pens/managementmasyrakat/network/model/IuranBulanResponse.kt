package com.pens.managementmasyrakat.network.model

import com.pens.managementmasyrakat.network.lib.DataResponse

data class IuranBulanResponse(
    val bulan_id: Int,
    val created_at: String,
    val id: Int,
    val sampah: Boolean,
    val sosial: Boolean,
    val tahun: String,
    val updated_at: String,
    val user_id: Int
) : DataResponse<IuranBulanResponse> {
    override fun retrieveData(): IuranBulanResponse = this
}