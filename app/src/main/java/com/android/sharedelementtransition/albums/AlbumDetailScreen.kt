package com.android.sharedelementtransition.albums

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.android.sharedelementtransition.R
import com.android.sharedelementtransition.lerp
import com.android.sharedelementtransition.models.AlbumInfoModel
import com.android.sharedelementtransition.models.HeaderParams
import com.android.sharedelementtransition.models.SharedElementData
import com.android.sharedelementtransition.models.SharedElementParams
import com.android.sharedelementtransition.statusBarsPaddingWithOffset
import com.android.sharedelementtransition.toPx
import com.android.sharedelementtransition.ui.TopMenu
import kotlinx.coroutines.launch

const val ANIM_DURATION = 400

@Composable
fun AlbumDetailScreen(
    maxContentWidth: Int,
    sharedElementData: SharedElementData,
    transitioned: Boolean,
    onTransitionFinished: () -> Unit,
    onBackClick: () -> Unit
) {
    val sharedElementTargetSize = LocalConfiguration.current.screenHeightDp.dp

    AlbumDetailScreen(
        albumInfo = sharedElementData.albumInfo,
        isAppearing = transitioned,
        sharedElementParams = SharedElementParams(
            initialOffset = Offset(
                sharedElementData.offSetX.toPx(LocalDensity.current).toFloat(),
                sharedElementData.offSetY.toPx(LocalDensity.current).toFloat(),
            ),
            targetOffset = Offset(
                x = (maxContentWidth - sharedElementTargetSize.toPx(LocalDensity.current)) / 2f,
                y = 0f
            ),
            initialSize = sharedElementData.size,
            targetSize = sharedElementTargetSize
        ),
        onBackClick = onBackClick,
        onTransitionFinished = onTransitionFinished
    )
}

@Composable
fun AlbumDetailScreen(
    modifier: Modifier = Modifier,
    albumInfo: AlbumInfoModel,
    sharedElementParams: SharedElementParams,
    isAppearing: Boolean,
    onTransitionFinished: () -> Unit,
    onBackClick: () -> Unit
) {
    val sharedElementProgress = remember { Animatable(if (isAppearing) 0f else 1f) }
    val titleProgress = remember { Animatable(if (isAppearing) 0f else 1f) }
    val bgColorProgress = remember { Animatable(if (isAppearing) 0f else 1f) }
    val offsetProgress = remember { Animatable(if (isAppearing) 0f else 1f) }

    val headerParams = remember {
        HeaderParams(
            sharedElementParams = sharedElementParams,
            coverId = albumInfo.cover,
            title = albumInfo.title,
            author = albumInfo.author
        )
    }

    LaunchedEffect(key1 = isAppearing) {
        launch {
            sharedElementProgress.animateTo(
                if (isAppearing) 1f else 0f,
                animationSpec = tween(ANIM_DURATION)
            )
            onTransitionFinished()
        }

        launch {
            titleProgress.animateTo(
                if (isAppearing) 1f else 0f,
                animationSpec = tween(
                    durationMillis = ANIM_DURATION / 2,
                    delayMillis = if (isAppearing) ANIM_DURATION / 2 else 0
                )
            )
        }
        launch {
            bgColorProgress.animateTo(
                if (isAppearing) 1f else 0f,
                animationSpec = tween(
                    durationMillis = ANIM_DURATION,
                )
            )
        }
        launch {
            offsetProgress.animateTo(
                targetValue = if (isAppearing) 1f else 0f,
                animationSpec = tween(ANIM_DURATION),
            )
            onTransitionFinished()
        }
    }

    val currentSize = lerp(
        headerParams.sharedElementParams.initialSize,
        headerParams.sharedElementParams.targetSize,
        offsetProgress.value
    )

    val currentOffset = lerp(
        headerParams.sharedElementParams.initialOffset.copy(x = headerParams.sharedElementParams.initialOffset.x, y = headerParams.sharedElementParams.initialOffset.y),
        Offset(
            x = 0.dp.value,
            y = 0.dp.value
        ),
        offsetProgress.value
    )

    Surface(
        modifier = Modifier
            .size(currentSize)
            .offset { IntOffset(x = currentOffset.x.toInt(), y = currentOffset.y.toInt()) }
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TopMenu(
                modifier = Modifier
                    .statusBarsPaddingWithOffset()
                    .padding(horizontal = 16.dp),
                title = TOP_MENU_TITLE,
                endIconResId = R.drawable.baseline_share_24,
                onStartIconClick = onBackClick
            )

            Spacer(modifier = Modifier.height(10.dp))

            Image(
                modifier = Modifier.size(200.dp).clip(RoundedCornerShape(50.dp)),
                painter = painterResource(id = headerParams.coverId),
                contentDescription = "",
            )

            Spacer(modifier = Modifier.height(10.dp))

            Labels(
                title = headerParams.title,
                author = headerParams.author,
            )
        }
    }
}

@Composable
fun Labels(
    title: String,
    author: String,
) {
    Column(
        modifier = Modifier
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = author,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface,
        )
    }
}
