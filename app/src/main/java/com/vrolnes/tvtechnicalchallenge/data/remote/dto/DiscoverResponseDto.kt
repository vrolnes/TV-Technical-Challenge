package com.vrolnes.tvtechnicalchallenge.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DiscoverResponseDto(
    val page: Int,
    val results: List<MovieResultDto>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)