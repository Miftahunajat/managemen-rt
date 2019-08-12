package com.pens.managementmasyrakat.network.model

import com.pens.managementmasyrakat.network.lib.DataResponse

data class UserBayarArisan(
    val arisans_user_id: Int,
    val bayar: Boolean,
    val bulan_id: Int,
    val bulan: Bulan,
    val created_at: String,
    val id: Int,
    val tahun: String,
    val updated_at: String
) : DataResponse<UserBayarArisan> {
    override fun retrieveData(): UserBayarArisan = this
}