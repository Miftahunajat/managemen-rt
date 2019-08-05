package com.pens.managementmasyrakat.network.model

import com.pens.managementmasyrakat.network.lib.DataResponse

data class ListResponse<T>(val items: List<T>) : DataResponse<List<T>> {
    override fun retrieveData(): List<T> = items
}