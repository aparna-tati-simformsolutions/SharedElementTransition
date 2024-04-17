package com.android.sharedelementtransition.albums

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.sharedelementtransition.R
import com.android.sharedelementtransition.RoundedCornersSurface
import com.android.sharedelementtransition.lastIndex
import com.android.sharedelementtransition.models.AlbumInfoModel
import com.android.sharedelementtransition.models.PlayBackData
import com.android.sharedelementtransition.states.PlayerScreenState
import com.android.sharedelementtransition.statusBarsPaddingWithOffset
import com.android.sharedelementtransition.ui.TopMenu

const val TOP_MENU_TITLE = "Albums"

@Composable
fun AlbumScreen(
    screenState: PlayerScreenState,
    playBackData: PlayBackData,
    sharedProgress: Float,
    onBackClick: () -> Unit,
    onInfoClick: (albumInfo: AlbumInfoModel, offsetX: Float, offsetY: Float, size: Int) -> Unit
) {

    var clickedItemIndex by remember { mutableIntStateOf(-1) }
    val transitionInProgress by remember(sharedProgress) {
        derivedStateOf { sharedProgress > 0f }
    }

    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxSize()
            .height(screenState.albumContainerHeight.dp)
    ) {
        RoundedCornersSurface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary
        ) {
            Column {
                TopMenu(
                    modifier = Modifier
                        .statusBarsPaddingWithOffset()
                        .padding(horizontal = 16.dp),
                    title = TOP_MENU_TITLE,
                    endIconResId = R.drawable.baseline_share_24,
                    titleColor = MaterialTheme.colorScheme.onPrimary,
                    iconsTint = MaterialTheme.colorScheme.onPrimary,
                    onStartIconClick = onBackClick
                )
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(screenState.albumImageWidth)
                ) {
                    items(playBackData.albums.toMutableList()) { albumInfoModel ->
                        val index = playBackData.albums.indexOf(albumInfoModel)
                        Spacer(modifier = Modifier.width(if (index == 0) 24.dp else 16.dp))
                        val itemAlpha = if (clickedItemIndex == index && transitionInProgress) 0f else 1f
                        CompositionLocalProvider(LocalContentAlpha provides itemAlpha) {
                            AlbumListItem(
                                info = albumInfoModel,
                                albumImageWidth = screenState.albumImageWidth,
                                onClick = { info, offset, size ->
                                    clickedItemIndex = index
                                    onInfoClick(info, offset.x, offset.y, size)
                                }
                            )
                        }
                        if (index == playBackData.albums.lastIndex) {
                            Spacer(modifier = Modifier.width(24.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AlbumListItem(
    modifier: Modifier = Modifier,
    info: AlbumInfoModel,
    albumImageWidth: Dp,
    onClick: (info: AlbumInfoModel, offset: Offset, size: Int) -> Unit
) {
    var parentOffset by remember { mutableStateOf(Offset.Unspecified) }
    var mySize by remember { mutableIntStateOf(0) }
    Column(
        modifier = modifier.width(albumImageWidth).padding(10.dp),
    ) {
        Image(
            painter = painterResource(id = info.cover),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(1f)
                .onGloballyPositioned { coordinates ->
                    parentOffset = coordinates.positionInRoot()
                    mySize = coordinates.size.width
                }
                .clip(RoundedCornerShape(10.dp))
                .alpha(LocalContentAlpha.current)
                .clickable { onClick(info, parentOffset, mySize) },
        )
    }
}
