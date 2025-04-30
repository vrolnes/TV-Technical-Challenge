package com.vrolnes.tvtechnicalchallenge.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vrolnes.tvtechnicalchallenge.presentation.ui.MovieDetailScreen
import com.vrolnes.tvtechnicalchallenge.presentation.ui.MovieListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MovieList.route) {
        composable(route = Screen.MovieList.route) {
            MovieListScreen(navController = navController)
        }
        composable(
            route = Screen.MovieDetail.route,
            arguments = listOf(navArgument(Screen.MOVIE_ID_ARG) { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt(Screen.MOVIE_ID_ARG)
            if (movieId != null) {
                MovieDetailScreen(movieId = movieId, navController = navController)
            } else {
                 navController.popBackStack()
            }
        }
    }
} 