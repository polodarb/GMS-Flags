package ua.polodarb.gmsflags.ui.animations

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
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
        durationMillis = 350,
        delayMillis = 150,
        easing = FastOutSlowInEasing
    )
) + fadeIn(
    animationSpec = tween(
        durationMillis = 250,
        delayMillis = 250,
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
        durationMillis = 350,
        easing = FastOutSlowInEasing
    )
) + fadeOut(
    animationSpec = tween(
        durationMillis = 250,
        easing = FastOutSlowInEasing
    ),
    targetAlpha = 0.0f
)
