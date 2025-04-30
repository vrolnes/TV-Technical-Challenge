package com.vrolnes.tvtechnicalchallenge.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vrolnes.tvtechnicalchallenge.presentation.ui.navigation.AppNavigation
import com.vrolnes.tvtechnicalchallenge.presentation.ui.theme.TVTechnicalChallengeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TVTechnicalChallengeTheme {
                AppNavigation()
            }
        }
    }
}