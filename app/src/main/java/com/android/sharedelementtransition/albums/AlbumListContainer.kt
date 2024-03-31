package com.android.sharedelementtransition.albums

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.sharedelementtransition.R
import com.android.sharedelementtransition.RoundedCornersSurface
import com.android.sharedelementtransition.lastIndex
import com.android.sharedelementtransition.models.AlbumInfoModel
import com.android.sharedelementtransition.statusBarsPaddingWithOffset
import com.android.sharedelementtransition.ui.TopMenu

const val TOP_MENU_TITLE = "Albums"

@Composable
fun AlbumListContainer(
    modifier: Modifier = Modifier,
    listScrollState: ScrollState = rememberScrollState(),
    albumData: Collection<AlbumInfoModel>,
    albumImageWidth: Dp = 150.dp,
    transitionAnimationProgress: Float = 0f,
    appearingAnimationProgress: Float = 1f,
    onBackClick: () -> Unit = {},
    onShareClick: () ->  Unit = {},
    onInfoClick: (info: AlbumInfoModel, offsetX: Float, offsetY: Float, size: Int) -> Unit = { _, _, _, _ -> }
) {

    var clickedItemIndex by remember { mutableStateOf(-1) }
    val transitionInProgress by remember(transitionAnimationProgress) {
        derivedStateOf { transitionAnimationProgress > 0f }
    }

    RoundedCornersSurface(
        modifier = modifier,
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
                onStartIconClick = onBackClick,
                onEndIconClick = onShareClick,
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(modifier = Modifier.horizontalScroll(listScrollState)) {
                albumData.forEachIndexed { index, albumInfoModel ->
                    Spacer(modifier = Modifier.width(if (index == 0) 24.dp else 16.dp))
                    val itemAlpha = if (clickedItemIndex == index && transitionInProgress) 0f else 1f
                    CompositionLocalProvider(LocalContentAlpha provides itemAlpha) {
                        AlbumListItem(
                            info = albumInfoModel,
                            albumImageWidth = albumImageWidth,
                            onClick = { info, offset, size ->
                                clickedItemIndex = index
                                onInfoClick(info, offset.x, offset.y, size)
                            }
                        )
                    }
                    if (index == albumData.lastIndex) {
                        Spacer(modifier = Modifier.width(24.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun AlbumListItem(
    modifier: Modifier = Modifier,
    info: AlbumInfoModel,
    albumImageWidth: Dp,
    onClick: (info: AlbumInfoModel, offset: Offset, size: Int) -> Unit
) {
    var parentOffset by remember { mutableStateOf(Offset.Unspecified) }
    var mySize by remember { mutableStateOf(0) }
    Column(
        modifier = modifier.width(albumImageWidth),
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
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = info.title,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = info.year.toString(),
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
        )
    }
}