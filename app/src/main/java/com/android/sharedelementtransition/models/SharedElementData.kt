package com.android.sharedelementtransition.models

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
@Immutable
data class SharedElementData(
    val albumInfo: AlbumInfoModel,
    val offSetX: Dp,
    val offSetY: Dp,
    val size: Dp
) {
    companion object {
        val NONE = SharedElementData(AlbumInfoModel(0, "", "", 0, listOf()), 0.dp, 0.dp, 0.dp)
    }
}