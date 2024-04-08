package com.android.sharedelementtransition.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.min
import com.android.sharedelementtransition.toDp

enum class Screen {
    MAINPLAYERSCREEN,
    ALBUMPREVIEW
}
enum class AlbumPreviewDetailScreen {
    NONE,
    ALBUMPREVIEWDETAIL
}

@Composable
fun rememberPlayerScreenState(
    constraints: Constraints,
    density: Density = LocalDensity.current
) = remember(constraints) {
    PlayerScreenState(
        constraints,
        density
    )
}

class PlayerScreenState(
    constraints: Constraints,
    private val density: Density
) {
    @Stable
    private fun Float.toDp() = this.toDp(density)

    var maxContentWidth = constraints.maxWidth
    var maxContentHeight = constraints.maxHeight

    var currentDragOffset by mutableFloatStateOf(0f)

    var currentScreen by mutableStateOf(Screen.MAINPLAYERSCREEN)

    val albumContainerHeight = maxContentHeight

    val albumImageWidth =
        min((maxContentWidth * 0.35f).toDp(), (maxContentHeight * 0.16f).toDp())

    val backHandlerEnabled by derivedStateOf { currentScreen != Screen.MAINPLAYERSCREEN }
}
