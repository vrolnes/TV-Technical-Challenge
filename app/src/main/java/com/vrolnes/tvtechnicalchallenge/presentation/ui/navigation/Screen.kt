package com.vrolnes.tvtechnicalchallenge.presentation.ui.navigation

sealed class Screen(val route: String) {
    object MovieList : Screen("movie_list_screen")
    object MovieDetail : Screen("movie_detail_screen/{${Args.MOVIE_ID}}") {
        fun createRoute(movieId: Int) = "movie_detail_screen/$movieId"
    }
    object MoviePlayer : Screen("movie_player_screen/{${Args.MOVIE_ID}}") {
        fun createRoute(movieId: Int) = "movie_player_screen/$movieId"
    }

    // Use object for argument keys for consistency
    object Args {
        const val MOVIE_ID = "movieId"
    }
} 