package com.pens.managementmasyrakat.network.model

import com.pens.managementmasyrakat.network.lib.DataResponse

data class Arisan(
    val created_at: String,
    val id: Int,
    val iuran: String,
    val jenis_kelamin_id: Int,
    val mulai: String,
    val nama: String,
    val peserta: Int,
    val selesai: String,
    val updated_at: String,
    val tutup: String,
    val user_ikut: Boolean?,
    val arisans_users: List<AllUserArisanResponse>
) : DataResponse<Arisan> {
    override fun retrieveData(): Arisan = this
}

