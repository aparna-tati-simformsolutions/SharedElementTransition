package com.android.sharedelementtransition.models

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
@Immutable
class HeaderParams(
    val sharedElementParams: SharedElementParams,
    @DrawableRes val coverId: Int,
    val title: String,
    val author: String,
)