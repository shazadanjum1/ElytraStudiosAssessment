package com.elytrastudios.assessment.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer

@Composable
fun Modifier.shimmerEffect(): Modifier {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
    return this.shimmer(shimmer)
}
