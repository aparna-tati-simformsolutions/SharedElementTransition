package com.android.sharedelementtransition

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
fun Int.toDp(density: Density): Dp = with(density) { this@toDp.toDp() }

@Stable
fun Float.toDp(density: Density): Dp = with(density) { this@toDp.toDp() }

@Stable
val <E> Collection<E>.lastIndex
    get() = count() - 1

internal val defaultStatusBarPadding = 24.dp

fun Modifier.statusBarsPaddingWithOffset(additionalOffset: Dp = defaultStatusBarPadding): Modifier =
    this
        .padding(top = additionalOffset)
        .statusBarsPadding()