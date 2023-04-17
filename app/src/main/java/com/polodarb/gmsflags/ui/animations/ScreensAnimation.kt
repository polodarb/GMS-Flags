package com.polodarb.gmsflags.ui.animations

import android.graphics.Color.alpha
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.core.graphics.translationMatrix
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph

@ExperimentalAnimationApi
fun AnimatedContentTransitionScope<*>.enterAnimToRight(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): EnterTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph
    if (initialNavGraph.id != targetNavGraph.id) {
        return fadeIn()
    }

//    return fadeIn(tween(350)) + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
    return slideInHorizontally(
        initialOffsetX = { 400 },
        animationSpec = tween(300)
    ) + fadeIn(tween(400))
}

@ExperimentalAnimationApi
fun AnimatedContentTransitionScope<*>.enterAnimFade(): EnterTransition {
    return fadeIn(tween(350))
}

@ExperimentalAnimationApi
fun AnimatedContentTransitionScope<*>.exitAnimToLeft(
    initial: NavBackStackEntry,
    target: NavBackStackEntry,
): ExitTransition {
    val initialNavGraph = initial.destination.hostNavGraph
    val targetNavGraph = target.destination.hostNavGraph

//    return return fadeOut(tween(350)) + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
    return slideOutHorizontally(
        targetOffsetX = { 400 },
        animationSpec = tween(300)
    ) + fadeOut(tween(400))
}

@ExperimentalAnimationApi
fun AnimatedContentTransitionScope<*>.exitAnimFade(): ExitTransition {
    return fadeOut(tween(350))
}

private val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph

@ExperimentalAnimationApi
fun AnimatedContentTransitionScope<*>.popEnterAnimToRight(): EnterTransition {
    return fadeIn() + slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End)
}

@ExperimentalAnimationApi
fun AnimatedContentTransitionScope<*>.popExitAnimToLeft(): ExitTransition {
    return fadeOut() + slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
}
