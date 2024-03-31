package com.android.sharedelementtransition.albums

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.sharedelementtransition.DpInsets
import com.android.sharedelementtransition.lerp
import com.android.sharedelementtransition.models.AlbumInfoModel
import com.android.sharedelementtransition.models.HeaderParams
import com.android.sharedelementtransition.models.SharedElementData
import com.android.sharedelementtransition.models.SharedElementParams
import com.android.sharedelementtransition.toDp
import com.android.sharedelementtransition.toPx
import kotlinx.coroutines.launch

const val ANIM_DURATION = 500

@Composable
fun AlbumDetailScreen(
    maxContentWidth: Int,
    sharedElementData: SharedElementData,
    transitioned: Boolean,
    topInset: Dp,
    onTransitionFinished: () -> Unit,
    onBackClick: () -> Unit
) {
    val sharedElementTargetSize = 230.dp
    val insets = WindowInsets.navigationBars
    val density = LocalDensity.current
    val bottomInset by remember(insets) {
        derivedStateOf { insets.getBottom(density).toDp(density) }
    }

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
                y = 50.dp.toPx(LocalDensity.current).toFloat()
            ),
            initialSize = sharedElementData.size,
            targetSize = sharedElementTargetSize,
            initialCornerRadius = 10.dp,
            targetCornerRadius = sharedElementTargetSize / 2
        ),
        onBackClick = onBackClick,
        onTransitionFinished = onTransitionFinished,
        insets = DpInsets.from(
            topInset = topInset,
            bottomInset = bottomInset
        )
    )
}

@Composable
fun AlbumDetailScreen(
    modifier: Modifier = Modifier,
    albumInfo: AlbumInfoModel,
    sharedElementParams: SharedElementParams,
    isAppearing: Boolean,
    insets: DpInsets = DpInsets.Zero,
    onTransitionFinished: () -> Unit,
    onBackClick: () -> Unit
) {
    val sharedElementProgress = remember { Animatable(if (isAppearing) 0f else 1f) }
    val titleProgress = remember { Animatable(if (isAppearing) 0f else 1f) }
    val bgColorProgress = remember { Animatable(if (isAppearing) 0f else 1f) }

    val headerParams = remember {
        HeaderParams(
            sharedElementParams = sharedElementParams,
            coverId = albumInfo.cover,
            title = albumInfo.title,
            author = albumInfo.author
        )
    }

    val headerState =
        rememberCollapsingHeaderState(key = insets.topInset, topInset = insets.topInset)

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
    }

    val surfaceMaterialColor = MaterialTheme.colors.surface
    val surfaceMaterialColorTransparent = surfaceMaterialColor.copy(alpha = 0f)
    val surfaceColor = remember {
        derivedStateOf {
            lerp(
                surfaceMaterialColorTransparent,
                surfaceMaterialColor,
                bgColorProgress.value
            )
        }
    }

    val contentAlphaState = titleProgress.asState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = surfaceColor.value,
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Header(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerState.headerHeight),
                params = headerParams,
                contentAlphaProvider = contentAlphaState,
                backgroundColorProvider = surfaceColor,
                isAppearing = isAppearing,
                onBackClick = onBackClick,
            )
        }
    }
}
