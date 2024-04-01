package com.android.sharedelementtransition.models

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.android.sharedelementtransition.R

@Stable
@Immutable
class PlayBackData {
    val albums: Collection<AlbumInfoModel> = listOf(
        AlbumInfoModel(R.drawable.img_album_01, "It happened Quiet", "Aurora", 2018, songs()),
        AlbumInfoModel(R.drawable.img_album_02, "All My Daemons", "Aurora", 2016, songs()),
        AlbumInfoModel(R.drawable.img_album_03, "Running", "Aurora", 2015, songs()),
        AlbumInfoModel(R.drawable.img_album_04, "Running", "Aurora", 2015, songs()),
        AlbumInfoModel(R.drawable.img_album_05, "Running", "Aurora", 2015, songs()),
        AlbumInfoModel(R.drawable.img_album_06, "Running", "Aurora", 2015, songs()),
        AlbumInfoModel(R.drawable.img_album_07, "Running", "Aurora", 2015, songs()),
        AlbumInfoModel(R.drawable.img_album_08, "Running", "Aurora", 2015, songs())
    )

    private fun songs() = listOf(
        SongInfoModel(0L, "Aurora", "All Is Soft Inside", "3:54"),
        SongInfoModel(1L, "Aurora", "Queendom", "5:47"),
        SongInfoModel(2L, "Aurora", "Gentle Earthquakes", "4:32"),
        SongInfoModel(3L, "Aurora", "Awakening", "6:51"),
        SongInfoModel(4L, "Aurora", "All Is Soft Inside", "3:54"),
        SongInfoModel(5L, "Aurora", "Queendom", "5:47"),
        SongInfoModel(6L, "Aurora", "Gentle Earthquakes", "4:32"),
        SongInfoModel(7L, "Aurora", "Awakening", "6:51"),
        SongInfoModel(8L, "Aurora", "All Is Soft Inside", "3:54"),
        SongInfoModel(9L, "Aurora", "Queendom", "5:47"),
        SongInfoModel(10L, "Aurora", "Gentle Earthquakes", "4:32"),
        SongInfoModel(11L, "Aurora", "Awakening", "6:51"),
        SongInfoModel(12L, "Aurora", "All Is Soft Inside", "3:54"),
        SongInfoModel(13L, "Aurora", "Queendom", "5:47"),
        SongInfoModel(14L, "Aurora", "Gentle Earthquakes", "4:32"),
        SongInfoModel(15L, "Aurora", "Awakening", "6:51"),
    )
}