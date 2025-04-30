package com.vrolnes.tvtechnicalchallenge.data.mapper

import com.vrolnes.tvtechnicalchallenge.data.remote.dto.MovieResultDto
import com.vrolnes.tvtechnicalchallenge.domain.model.Movie

// Image quality suffix - could also be configured via DI
private const val IMAGE_QUALITY_SUFFIX = "w500"

// Now depends on the base image URL provided externally (e.g., via DI)
fun MovieResultDto.toDomain(imageBaseUrl: String): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        posterUrl = this.posterPath?.let { "$imageBaseUrl$IMAGE_QUALITY_SUFFIX$it" }
    )
}

// The list mapper now needs the base URL too
fun List<MovieResultDto>.toDomain(imageBaseUrl: String): List<Movie> {
    return this.map { it.toDomain(imageBaseUrl) }
} 