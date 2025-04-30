package com.vrolnes.tvtechnicalchallenge.presentation.ui.navigation

sealed class Screen(val route: String) {
    object MovieList : Screen("movie_list_screen")
    object MovieDetail : Screen("movie_detail_screen/{movieId}") {
        fun createRoute(movieId: Int) = "movie_detail_screen/$movieId"
    }

    companion object {
        const val MOVIE_ID_ARG = "movieId"
    }
} 