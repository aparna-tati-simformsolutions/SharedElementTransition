package com.android.sharedelementtransition.albums

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.android.sharedelementtransition.lerp
import com.android.sharedelementtransition.models.SharedElementParams
import com.android.sharedelementtransition.toDp
import com.android.sharedelementtransition.toPx
import kotlinx.coroutines.launch

@Composable
fun SharedElementContainer(
    params: SharedElementParams,
    isForward: Boolean,
    onTransitionFinished: () -> Unit = {},
    title: @Composable (BoxScope.() -> Unit),
    labels: @Composable (BoxScope.() -> Unit),
    sharedElement: @Composable (BoxScope.() -> Unit)
) {
    val density = LocalDensity.current
    val offsetProgress = remember { Animatable(if (isForward) 0f else 1f) }
    val cornersProgress = remember { Animatable(if (isForward) 0f else 1f) }
    LaunchedEffect(key1 = isForward) {
        launch {
            offsetProgress.animateTo(
                targetValue = if (isForward) 1f else 0f,
                animationSpec = tween(ANIM_DURATION),
            )
            onTransitionFinished()
        }
        launch {
            cornersProgress.animateTo(
                targetValue = if (isForward) 1f else 0f,
                animationSpec = tween(2 * ANIM_DURATION / 3),
            )
        }
    }

    val initialOffset = params.initialOffset.copy(y = params.initialOffset.y)

    val cornersSize = lerp(
        params.initialCornerRadius,
        params.targetCornerRadius,
        cornersProgress.value,
    )

    val sharedElementSize = params.targetSize

    val currentSize = lerp(
        params.initialSize,
        sharedElementSize,
        offsetProgress.value
    )

    val targetOffset = Offset(
        x = (LocalConfiguration.current.screenWidthDp / 2).toFloat(),
        y = 128.dp.toPx(density).toFloat())

    val currentOffset = lerp(
        initialOffset,
        targetOffset,
        offsetProgress.value
    )

    SharedElementContainer(
        coverOffset = currentOffset,
        coverSize = currentSize,
        coverCornersRadius = cornersSize,
        title = title,
        labels = labels,
        sharedElement = sharedElement,
    )
}

@Composable
fun SharedElementContainer(
    modifier: Modifier = Modifier,
    coverOffset: Offset,
    coverSize: Dp,
    coverCornersRadius: Dp,
    title: @Composable BoxScope.() -> Unit,
    labels: @Composable BoxScope.() -> Unit,
    sharedElement: @Composable BoxScope.() -> Unit,
) {
    val density = LocalDensity.current

    Box(
        modifier = modifier,
    ) {
        title()
        Column {
            Box(
                modifier = modifier
                    .padding(top = coverOffset.y.toDp(density))
                    .offset {
                        IntOffset(x = coverOffset.x.toInt(), y = 0)
                    }
                    .size(coverSize)
                    .clip(RoundedCornerShape(coverCornersRadius)),
                content = sharedElement,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                content = labels,
            )
        }
    }
}