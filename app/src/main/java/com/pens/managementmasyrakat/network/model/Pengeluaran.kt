package com.pens.managementmasyrakat.network.model

import com.pens.managementmasyrakat.network.lib.DataResponse

data class Pengeluaran(
    val created_at: String,
    val id: Int,
    val jumlah: String,
    val keterangan: String,
    val pengeluaran_id: Int,
    val tanggal: String,
    val updated_at: String
) : DataResponse<Pengeluaran> {
    override fun retrieveData(): Pengeluaran = this
}