package com.vrolnes.tvtechnicalchallenge.presentation.viewmodel

import android.app.Application
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.drm.DefaultDrmSessionManager
import androidx.media3.exoplayer.drm.DrmSessionManager
import androidx.media3.exoplayer.drm.FrameworkMediaDrm
import androidx.media3.exoplayer.drm.HttpMediaDrmCallback
import androidx.media3.exoplayer.dash.DashMediaSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// Static URLs from challenge description (Consider moving these to a constants file)
private const val VOD_URI = "https://storage.googleapis.com/wvmedia/cenc/h264/tears/tears.mpd"
private const val DRM_LICENSE_URI = "https://proxy.uat.widevine.com/proxy?video_id=2015_tears&provider=widevine_test"
private val DRM_SCHEME_UUID = Util.getDrmUuid("widevine")!!


@OptIn(UnstableApi::class) // For ExoPlayer APIs
@HiltViewModel
class MoviePlayerViewModel @Inject constructor(
    // Use Application context for longer lifecycle needs like player
    private val application: Application
) : ViewModel() {

    val player: ExoPlayer

    init {
        player = buildExoPlayer()
        preparePlayer()
    }

    private fun buildExoPlayer(): ExoPlayer {
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
        val drmCallback = HttpMediaDrmCallback(DRM_LICENSE_URI, httpDataSourceFactory)

        val drmSessionManager: DrmSessionManager = DefaultDrmSessionManager.Builder()
            .setUuidAndExoMediaDrmProvider(DRM_SCHEME_UUID, FrameworkMediaDrm.DEFAULT_PROVIDER)
            .setMultiSession(false) // Required for Widevine test license server
            .build(drmCallback)

        val mediaSourceFactory = DashMediaSource.Factory(httpDataSourceFactory)
            .setDrmSessionManagerProvider { drmSessionManager }

        return ExoPlayer.Builder(application) // Use Application context
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
    }

    private fun preparePlayer() {
        val mediaItem = MediaItem.Builder()
            .setUri(VOD_URI)
            .setDrmConfiguration(
                MediaItem.DrmConfiguration.Builder(DRM_SCHEME_UUID)
                    .build()
            )
            .build()

        player.setMediaItem(mediaItem)
        player.playWhenReady = true // Start playback automatically
        player.prepare()
    }

    // Release the player when the ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        if (player.isCommandAvailable(ExoPlayer.COMMAND_RELEASE)) {
             player.release()
        }
    }
} 