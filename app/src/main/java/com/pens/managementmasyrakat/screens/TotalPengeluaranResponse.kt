package com.pens.managementmasyrakat.screens

import com.pens.managementmasyrakat.network.lib.DataResponse

data class TotalPengeluaranResponse(
    val total: Int
) : DataResponse<TotalPengeluaranResponse> {
    override fun retrieveData(): TotalPengeluaranResponse = this
}