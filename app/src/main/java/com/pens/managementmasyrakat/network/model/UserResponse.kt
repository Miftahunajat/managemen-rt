package com.pens.managementmasyrakat.network.model

import com.pens.managementmasyrakat.network.lib.DataResponse

data class UserResponse(
    val alamat: String,
    val created_at: String,
    val id: Int,
    val image_url: ImageUrl,
    val jenis_kelamin_id: Int,
    val nama: String,
    val no_hp: String,
    val password: String,
    val roles: List<Role>,
    val updated_at: String,
    val user_kk_id: Int
) : DataResponse<UserResponse> {
    override fun retrieveData(): UserResponse = this
}