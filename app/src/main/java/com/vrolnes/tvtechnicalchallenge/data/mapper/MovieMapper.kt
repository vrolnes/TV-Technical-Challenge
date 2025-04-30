package com.vrolnes.tvtechnicalchallenge.data.mapper

import com.vrolnes.tvtechnicalchallenge.data.remote.dto.MovieResultDto
import com.vrolnes.tvtechnicalchallenge.domain.model.Movie

// Image quality suffixes
private const val POSTER_IMAGE_QUALITY = "w500"
private const val BACKDROP_IMAGE_QUALITY = "w780"

/**
 * Maps a MovieResultDto (from /discover endpoint) to a Movie domain model.
 * Includes detail fields available in the discover response.
 */
fun MovieResultDto.toDomain(imageBaseUrl: String): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        posterUrl = this.posterPath?.let { "$imageBaseUrl$POSTER_IMAGE_QUALITY$it" },
        overview = this.overview,
        rating = this.voteAverage,
        releaseDate = this.releaseDate,
        backdropUrl = this.backdropPath?.let { "$imageBaseUrl$BACKDROP_IMAGE_QUALITY$it" }
    )
}

/**
 * Maps a list of MovieResultDto to a list of Movie domain models.
 */
fun List<MovieResultDto>.toDomain(imageBaseUrl: String): List<Movie> {
    return this.map { it.toDomain(imageBaseUrl) }
}