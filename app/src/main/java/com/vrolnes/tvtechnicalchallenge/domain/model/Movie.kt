package com.vrolnes.tvtechnicalchallenge.domain.model

import java.math.BigDecimal
import java.math.RoundingMode

data class Movie(
    val id: Int,
    val title: String,
    val posterUrl: String?,
    val overview: String? = null,
    val rating: Double? = null,
    val releaseDate: String? = null,
    val backdropUrl: String? = null
) {
    // Helper to get rating formatted to one decimal place
    val formattedRating: String?
        get() = rating?.let {
            BigDecimal(it).setScale(1, RoundingMode.HALF_UP).toDouble().toString()
        }
} 