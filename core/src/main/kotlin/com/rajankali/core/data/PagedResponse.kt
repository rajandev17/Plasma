package com.rajankali.core.data

import com.google.gson.annotations.SerializedName

data class PagedResponse<T>(
    @SerializedName("page") val currentPage: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val results: List<T>
)