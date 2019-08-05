package com.pens.managementmasyrakat.network.model

import com.pens.managementmasyrakat.network.lib.DataResponse

data class IuranPerTahunResponse(
    val iuran_per_tahun: List<IuranPerTahun>,
    val nama: String,
    val tahun: String
) : DataResponse<IuranPerTahunResponse> {
    override fun retrieveData(): IuranPerTahunResponse= this
}
