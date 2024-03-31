package com.android.sharedelementtransition

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animate
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
import com.android.sharedelementtransition.albums.AlbumScreen
import com.android.sharedelementtransition.states.AlbumPreviewDetailScreen
import com.android.sharedelementtransition.models.PlayBackData
import com.android.sharedelementtransition.models.SharedElementData
import com.android.sharedelementtransition.states.Screen
import com.android.sharedelementtransition.states.rememberPlayerScreenState
import kotlinx.coroutines.launch
import kotlin.math.abs

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

        fun animateOffset(initialValue: Float, targetValue: Float, onEnd: () -> Unit) {
            val distance = abs(targetValue - initialValue)
            val distancePercent = distance / screenState.maxContentWidth
            val duration = (250 * distancePercent).toInt()

            animScope.launch {
                animate(
                    initialValue = initialValue,
                    targetValue = targetValue,
                    animationSpec = tween(duration),
                ) { value, _ -> screenState.currentDragOffset = value }
                onEnd()
            }
        }

        fun collapse() {
            animateOffset(
                initialValue = screenState.currentDragOffset, targetValue = 0f
            ) {
                screenState.currentScreen = Screen.MAINPLAYERSCREEN
            }
        }

        fun animateTitleProgress(targetValue: Float) {
            animScope.launch {
                titleProgressForward.animateTo(
                    targetValue = targetValue,
                    animationSpec = tween(500),
                )
            }
        }

        MainPlayerScreen(screenState = screenState)

        if (screenState.currentScreen != Screen.MAINPLAYERSCREEN) {
            val density = LocalDensity.current
            AlbumScreen(
                screenState = screenState,
                playBackData = playBackData,
                sharedProgress = sharedProgress,
                onBackClick = {
                    screenState.currentScreen = Screen.MAINPLAYERSCREEN
                    collapse()
                },
                onInfoClick = { data, x, y, size ->
                    sharedElementParams = SharedElementData(
                        data, x.toDp(density), y.toDp(density), size.toDp(density)
                    )
                    sharedElementTransitioned = true
                    albumPreviewDetailTransition = AlbumPreviewDetailScreen.ALBUMPREVIEWDETAIL
                    animateTitleProgress(1f)
                }
            )
        }

        if (albumPreviewDetailTransition == AlbumPreviewDetailScreen.ALBUMPREVIEWDETAIL) {

        }
    }
}