package com.android.sharedelementtransition.models

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.android.sharedelementtransition.R

@Stable
@Immutable
class PlayBackData {
    val albums: Collection<AlbumInfoModel> = listOf(
        AlbumInfoModel(R.drawable.img_album_01, "It happened Quiet", "Aurora", 2018),
        AlbumInfoModel(R.drawable.img_album_02, "All My Daemons", "Aurora", 2016),
        AlbumInfoModel(R.drawable.img_album_03, "Running", "Aurora", 2015),
        AlbumInfoModel(R.drawable.img_album_04, "Running", "Aurora", 2015),
        AlbumInfoModel(R.drawable.img_album_05, "Running", "Aurora", 2015),
        AlbumInfoModel(R.drawable.img_album_06, "Running", "Aurora", 2015),
        AlbumInfoModel(R.drawable.img_album_07, "Running", "Aurora", 2015),
        AlbumInfoModel(R.drawable.img_album_08, "Running", "Aurora", 2015)
    )
}