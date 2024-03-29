package com.android.sharedelementtransition

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.android.sharedelementtransition.states.PlayerScreenState
import com.android.sharedelementtransition.states.Screen

@Composable
fun MainPlayerScreen(screenState: PlayerScreenState) {
    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = {
            screenState.currentScreen = Screen.ALBUMPREVIEW
        }) {
            Text(text = "Click")
        }
    }
}