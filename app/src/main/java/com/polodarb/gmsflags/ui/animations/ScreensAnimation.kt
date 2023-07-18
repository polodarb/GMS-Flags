package com.polodarb.gmsflags.ui.animations

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

@ExperimentalAnimationApi
fun enterAnim(
    toLeft: Boolean
) = slideInHorizontally(
    initialOffsetX = { (if (toLeft) 1 else -1) * (it * 0.075).toInt() },
    animationSpec = tween(
        durationMillis = 300,
        delayMillis = 100,
        easing = FastOutSlowInEasing
    )
) + fadeIn(
    animationSpec = tween(
        durationMillis = 200,
        delayMillis = 200,
        easing = FastOutSlowInEasing
    ),
    initialAlpha = 0.0f
)

@ExperimentalAnimationApi
fun exitAnim(
    toLeft: Boolean
) = slideOutHorizontally(
    targetOffsetX = { (if (!toLeft) 1 else -1) * (it * 0.075).toInt() },
    animationSpec = tween(
        durationMillis = 300,
        easing = LinearOutSlowInEasing
    )
) + fadeOut(
    animationSpec = tween(
        durationMillis = 200,
        easing = LinearOutSlowInEasing
    ),
    targetAlpha = 0.0f
)
