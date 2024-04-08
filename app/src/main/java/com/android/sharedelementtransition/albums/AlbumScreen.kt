package com.android.sharedelementtransition.albums

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.sharedelementtransition.models.AlbumInfoModel
import com.android.sharedelementtransition.models.PlayBackData
import com.android.sharedelementtransition.states.PlayerScreenState

@Composable
fun AlbumScreen(
    screenState: PlayerScreenState,
    playBackData: PlayBackData,
    sharedProgress: Float,
    onBackClick: () -> Unit,
    onInfoClick: (albumInfo: AlbumInfoModel, offsetX: Float, offsetY: Float, size: Int) -> Unit
) {
    AlbumScreenCustom(
        modifier = Modifier.background(Color.Transparent),
        screenState = screenState
    ) {
        AlbumListContainer(
            modifier = Modifier.fillMaxSize(),
            albumData = playBackData.albums,
            transitionAnimationProgress = sharedProgress,
            albumImageWidth = screenState.albumImageWidth,
            onBackClick = onBackClick,
            onInfoClick = onInfoClick
        )
    }
}

@Composable
fun AlbumScreenCustom(
    modifier: Modifier,
    screenState: PlayerScreenState,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .height(screenState.albumContainerHeight.dp)
    ) {
        content()
    }
}