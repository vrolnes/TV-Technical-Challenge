package com.vrolnes.tvtechnicalchallenge.presentation.ui

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import com.vrolnes.tvtechnicalchallenge.domain.model.Movie
import com.vrolnes.tvtechnicalchallenge.presentation.viewmodel.MovieListViewModel
import com.vrolnes.tvtechnicalchallenge.presentation.viewmodel.MoviePlayerViewModel
import com.vrolnes.tvtechnicalchallenge.util.findActivity

@Composable
fun MoviePlayerScreen(
    movieId: Int?, // Nullable to handle potential navigation issues
    movieListViewModel: MovieListViewModel = hiltViewModel(),
    playerViewModel: MoviePlayerViewModel = hiltViewModel()
) {
    val movie by if (movieId != null) {
        movieListViewModel.getMovieDetailsFlow(movieId).collectAsState(initial = null)
    } else {
        // Handle the case where movieId is null, maybe return a flow of null
        // For simplicity, using a dummy state here, adjust as needed
        kotlinx.coroutines.flow.MutableStateFlow<Movie?>(null).collectAsState()
    }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // Get player instance from the ViewModel
    val player = playerViewModel.player

    // Effect to manage player lifecycle based on screen lifecycle
    DisposableEffect(lifecycleOwner, player) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> player.pause()
                Lifecycle.Event.ON_RESUME -> player.play()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Effect to manage screen orientation
    DisposableEffect(Unit) {
        val activity = context.findActivity()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR

        onDispose {}
    }

    Scaffold { paddingValues ->
        if (isLandscape) {
            // Fullscreen player in landscape
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                PlayerViewContainer(player = player, modifier = Modifier.fillMaxSize())
            }
        } else {
            // Player + description in portrait
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                PlayerViewContainer(
                    player = player,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16 / 9f) // Maintain aspect ratio
                )
                if (movie != null) {
                    val currentMovie = movie // Capture the value locally
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = currentMovie?.title.toString(), // Use the local variable
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentMovie?.overview ?: "No overview available.", // Use the local variable
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@OptIn(UnstableApi::class) // For PlayerView
@Composable
private fun PlayerViewContainer(player: androidx.media3.exoplayer.ExoPlayer, modifier: Modifier = Modifier) {
    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                // Set player during initial creation
                this.player = player
                useController = true
            }
        },
        // Add the update block to re-apply the player on recomposition
        update = { view ->
            view.player = player
            view.useController = true // Ensure controller stays enabled
        },
        modifier = modifier
    )
}