package com.pens.managementmasyrakat.network.model

import com.pens.managementmasyrakat.network.lib.DataResponse

data class IuranBulanIniResponse(
    val iuran_arisan: Int,
    val iuran_sampah_bulan_ini: Boolean,
    val iuran_sosial_bulan_ini: Boolean
) : DataResponse<IuranBulanIniResponse> {
    override fun retrieveData(): IuranBulanIniResponse = this
}
