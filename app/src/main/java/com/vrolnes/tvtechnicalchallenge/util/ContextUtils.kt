package com.vrolnes.tvtechnicalchallenge.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Extension function to find the Activity from a Context.
 * Useful within Composables via LocalContext.current.findActivity().
 */
fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
} 