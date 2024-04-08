package com.android.sharedelementtransition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.compositionLocalOf
import com.android.sharedelementtransition.models.PlayBackData
import com.android.sharedelementtransition.ui.theme.SharedElementTransitionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SharedElementTransitionTheme(darkTheme = false) {
                PlayerScreen(playBackData = PlayBackData())
            }
        }
    }
}
