package com.pens.managementmasyrakat.network.model

import com.pens.managementmasyrakat.network.lib.DataResponse

data class PengungumanResponse(
    val body: String,
    val created_at: String,
    val id: Int,
    val image_description: String,
    val image_url: ImageUrl,
    val tanggal: String,
    val title: String,
    val updated_at: String
) : DataResponse<PengungumanResponse> {
    override fun retrieveData(): PengungumanResponse = this
}