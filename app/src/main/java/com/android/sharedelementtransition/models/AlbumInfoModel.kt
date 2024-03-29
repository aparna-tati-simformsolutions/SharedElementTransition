package com.android.sharedelementtransition.models

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
@Immutable
data class AlbumInfoModel(
    @DrawableRes val cover: Int,
    val title: String,
    val author: String,
    val year: Int,
    val songs: List<SongInfoModel>,
)
