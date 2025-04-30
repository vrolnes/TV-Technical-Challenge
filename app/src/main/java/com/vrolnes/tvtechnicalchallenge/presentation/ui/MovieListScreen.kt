package com.vrolnes.tvtechnicalchallenge.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vrolnes.tvtechnicalchallenge.R
import com.vrolnes.tvtechnicalchallenge.domain.model.Movie
import com.vrolnes.tvtechnicalchallenge.presentation.ui.components.MovieCard
import com.vrolnes.tvtechnicalchallenge.presentation.viewmodel.MovieListState
import com.vrolnes.tvtechnicalchallenge.presentation.viewmodel.MovieListViewModel
import com.vrolnes.tvtechnicalchallenge.presentation.viewmodel.MovieListViewModel.SortBy

// Threshold for triggering pagination (e.g., load next page when X items are left)
private const val HORIZONTAL_PAGINATION_THRESHOLD = 4

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        MovieListContent(
            state = state,
            onLoadNextPage = viewModel::loadNextPageForCategory,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun MovieListContent(
    state: MovieListState,
    onLoadNextPage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Popular Section
        item {
            MovieSection(
                title = "Popular",
                movies = state.popularMovies,
                isLoading = state.isPopularLoading,
                isLoadingNextPage = state.isPopularLoadingNextPage,
                error = state.popularError,
                endReached = state.popularEndReached,
                onLoadNextPage = { onLoadNextPage(SortBy.POPULARITY_DESC) }
            )
        }

        // Top Rated Section
        item {
            MovieSection(
                title = "Top Rated",
                movies = state.topRatedMovies,
                isLoading = state.isTopRatedLoading,
                isLoadingNextPage = state.isTopRatedLoadingNextPage,
                error = state.topRatedError,
                endReached = state.topRatedEndReached,
                onLoadNextPage = { onLoadNextPage(SortBy.VOTE_AVERAGE_DESC) }
            )
        }

        // Revenue Section
        item {
            MovieSection(
                title = "Revenue",
                movies = state.revenueMovies,
                isLoading = state.isRevenueLoading,
                isLoadingNextPage = state.isRevenueLoadingNextPage,
                error = state.revenueError,
                endReached = state.revenueEndReached,
                onLoadNextPage = { onLoadNextPage(SortBy.REVENUE_DESC) }
            )
        }

    }
}

/**
 * Composable for displaying a single movie category section (Title + Horizontal Carousel).
 */
@Composable
private fun MovieSection(
    title: String,
    movies: List<Movie>,
    isLoading: Boolean,
    isLoadingNextPage: Boolean,
    error: String?,
    endReached: Boolean,
    onLoadNextPage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()

    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (error != null && !isLoading) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        Box(modifier = Modifier.height(200.dp).fillMaxWidth()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (movies.isEmpty() && error == null) {
                Text(
                    text = "No movies found for $title",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center).padding(horizontal = 16.dp)
                )
            } else if (movies.isNotEmpty()) {
                LazyRow(
                    state = lazyListState,
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(movies, key = { "${title}_${it.id}" }) { movie ->
                        MovieCard(
                            movie = movie,
                            modifier = Modifier.width(130.dp)
                        )
                    }

                    if (isLoadingNextPage) {
                        item {
                            Box(modifier = Modifier.size(130.dp, 200.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }

                // Pagination Trigger Logic
                val shouldLoadMore by remember {
                    derivedStateOf {
                        val layoutInfo = lazyListState.layoutInfo
                        val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                        lastVisibleItem != null &&
                                !isLoadingNextPage &&
                                !endReached &&
                                // Check if the index is valid before calculation
                                layoutInfo.totalItemsCount > 0 &&
                                lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - HORIZONTAL_PAGINATION_THRESHOLD
                    }
                }

                LaunchedEffect(shouldLoadMore) {
                    if (shouldLoadMore) {
                        onLoadNextPage()
                    }
                }
            }
          }
    }
} 