package com.pens.managementmasyrakat.network.model

import com.pens.managementmasyrakat.network.lib.DataResponse

data class IkutArisanResponse(
    val arisan_id: Int,
    val arisan: Arisan,
    val created_at: String,
    val daftar: Boolean,
    val id: Int,
    val ikut: Boolean,
    val tarik: Boolean,
    val updated_at: String,
    val user_bayar_arisans: List<UserBayarArisan>,
    val user_id: Int,
    val user: UserResponse
) : DataResponse<IkutArisanResponse> {
    override fun retrieveData(): IkutArisanResponse = this
}