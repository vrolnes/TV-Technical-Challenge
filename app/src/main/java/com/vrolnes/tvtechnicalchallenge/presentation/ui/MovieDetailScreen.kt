package com.vrolnes.tvtechnicalchallenge.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vrolnes.tvtechnicalchallenge.domain.model.Movie
import com.vrolnes.tvtechnicalchallenge.presentation.viewmodel.MovieListViewModel

@Composable
fun MovieDetailScreen(
    movieId: Int,
    navController: NavController,
    listViewModel: MovieListViewModel = hiltViewModel()
) {
    val listState by listViewModel.state.collectAsStateWithLifecycle()

    val movie = remember(listState, movieId) {
        listState.popularMovies.find { it.id == movieId }
            ?: listState.topRatedMovies.find { it.id == movieId }
            ?: listState.revenueMovies.find { it.id == movieId }
    }

    MovieDetailContent(movie = movie, onNavigateBack = { navController.popBackStack() })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieDetailContent(
    movie: Movie?,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(movie?.title ?: "Movie Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        if (movie == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text("Movie not found.")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(movie.backdropUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "${movie.title} backdrop",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                    contentScale = ContentScale.Crop,
                   )

                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    // Title
                    Text(
                        text = movie.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Rating and Release Date Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Rating
                        movie.formattedRating?.let {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Rating",
                                    tint = Color.Yellow,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = it, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                        // Release Date
                        movie.releaseDate?.takeIf { it.isNotBlank() }?.let {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.DateRange,
                                    contentDescription = "Release Date",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = it, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Overview
                    Text(
                        text = movie.overview?.takeIf { it.isNotBlank() } ?: "No overview available.", // Added check for blank overview
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}