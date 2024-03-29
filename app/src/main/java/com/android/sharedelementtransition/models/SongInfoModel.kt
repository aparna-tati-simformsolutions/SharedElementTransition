package com.android.sharedelementtransition.models

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
@Immutable
data class SongInfoModel(
    val id: Long = 0L,
    val author: String,
    val title: String,
    val duration: String,
)