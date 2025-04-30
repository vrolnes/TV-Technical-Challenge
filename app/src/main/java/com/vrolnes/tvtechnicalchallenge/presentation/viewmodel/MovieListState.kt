package com.vrolnes.tvtechnicalchallenge.presentation.viewmodel

import com.vrolnes.tvtechnicalchallenge.domain.model.Movie

/**
 * Represents the state of the movie list screen, holding data for different categories.
 */
data class MovieListState(
    // Popular Movies Section
    val popularMovies: List<Movie> = emptyList(),
    val popularCurrentPage: Int = 1,
    val isPopularLoading: Boolean = false,
    val isPopularLoadingNextPage: Boolean = false,
    val popularEndReached: Boolean = false,
    val popularError: String? = null,

    // Top Rated Movies Section
    val topRatedMovies: List<Movie> = emptyList(),
    val topRatedCurrentPage: Int = 1,
    val isTopRatedLoading: Boolean = false,
    val isTopRatedLoadingNextPage: Boolean = false,
    val topRatedEndReached: Boolean = false,
    val topRatedError: String? = null,

    // Revenue Movies Section
    val revenueMovies: List<Movie> = emptyList(),
    val revenueCurrentPage: Int = 1,
    val isRevenueLoading: Boolean = false,
    val isRevenueLoadingNextPage: Boolean = false,
    val revenueEndReached: Boolean = false,
    val revenueError: String? = null,
) 