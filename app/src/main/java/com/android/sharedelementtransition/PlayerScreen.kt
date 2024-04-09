package com.android.sharedelementtransition

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.android.sharedelementtransition.albums.AlbumDetailScreen
import com.android.sharedelementtransition.albums.AlbumScreen
import com.android.sharedelementtransition.states.AlbumPreviewDetailScreen
import com.android.sharedelementtransition.models.PlayBackData
import com.android.sharedelementtransition.models.SharedElementData
import com.android.sharedelementtransition.states.Screen
import com.android.sharedelementtransition.states.rememberPlayerScreenState
import kotlinx.coroutines.launch

@Composable
fun PlayerScreen(playBackData: PlayBackData = PlayBackData()) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize())
    {
        val screenState = rememberPlayerScreenState(constraints, LocalDensity.current)

        var albumPreviewDetailTransition by remember { mutableStateOf(AlbumPreviewDetailScreen.NONE) }
        val animScope = rememberCoroutineScope()

        var sharedElementTransitioned by remember { mutableStateOf(false) }
        var sharedElementParams by remember { mutableStateOf(SharedElementData.NONE) }

        val titleProgressForward = remember { Animatable(0f) }
        val sharedProgress = titleProgressForward.value

        fun animateSharedTitleProgress(targetValue: Float) {
            animScope.launch {
                titleProgressForward.animateTo(
                    targetValue = targetValue,
                    animationSpec = tween(500),
                )
            }
        }

        val goBackFromNowPlayingScreen: () -> Unit = remember {
            {
                sharedElementTransitioned = false
                animateSharedTitleProgress(0f)
            }
        }

        if (screenState.currentScreen == Screen.MAINPLAYERSCREEN) {
            MainPlayerScreen(screenState = screenState)
        }

        if (screenState.currentScreen == Screen.ALBUMPREVIEW) {
            val density = LocalDensity.current
            AlbumScreen(
                screenState = screenState,
                playBackData = playBackData,
                sharedProgress = sharedProgress,
                onBackClick = {
                    screenState.currentScreen = Screen.MAINPLAYERSCREEN
                },
                onInfoClick = { data, x, y, size ->
                    sharedElementParams = SharedElementData(
                        data, x.toDp(density), y.toDp(density), size.toDp(density)
                    )
                    sharedElementTransitioned = true
                    albumPreviewDetailTransition = AlbumPreviewDetailScreen.ALBUMPREVIEWDETAIL
                    animateSharedTitleProgress(1f)
                }
            )
        }

        if (albumPreviewDetailTransition == AlbumPreviewDetailScreen.ALBUMPREVIEWDETAIL) {
            AlbumDetailScreen(
                maxContentWidth = screenState.maxContentWidth,
                sharedElementData = sharedElementParams,
                transitioned = sharedElementTransitioned,
                onTransitionFinished = {
                    if (!sharedElementTransitioned) {
                        albumPreviewDetailTransition = AlbumPreviewDetailScreen.NONE
                    }
                },
                onBackClick = goBackFromNowPlayingScreen
            )
        }

        BackHandler(screenState.backHandlerEnabled) {
            when {
                sharedElementTransitioned -> goBackFromNowPlayingScreen()
                screenState.currentScreen != Screen.MAINPLAYERSCREEN -> {
                    screenState.currentScreen = Screen.MAINPLAYERSCREEN
                }
            }
        }
    }
}