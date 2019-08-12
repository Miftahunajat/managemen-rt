package com.pens.managementmasyrakat.network.model

import com.pens.managementmasyrakat.network.lib.DataResponse

data class AllStatusArisanUser(
    val created_at: String,
    val id: Int,
    val iuran: String,
    val jenis_kelamin_id: Int,
    val mulai: String,
    val nama: String,
    val peserta: Int,
    val peserta_belum_membayar: List<UserResponse>,
    val peserta_sudah_ditarik: List<UserResponse>,
    val peserta_sudah_membayar: List<UserResponse>,
    val peserta_verifikasi: List<UserResponse>,
    val selesai: String,
    val updated_at: String
) : DataResponse<AllStatusArisanUser> {
    override fun retrieveData(): AllStatusArisanUser = this
}