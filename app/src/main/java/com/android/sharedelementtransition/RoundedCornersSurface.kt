package com.android.sharedelementtransition

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.sharedelementtransition.ui.theme.SharedElementTransitionTheme

@Composable
fun RoundedCornersSurface(
    modifier: Modifier = Modifier,
    topPadding: Dp = 0.dp,
    elevation: Dp = 0.dp,
    color: Color = MaterialTheme.colorScheme.surface,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize()
            .shadow(
                elevation = elevation,
                clip = true
            )
            .background(color)
            .padding(top = topPadding),
        content = content
    )
}

@Composable
@Preview
private fun PreviewRoundedCornersSurface() {
    SharedElementTransitionTheme(darkTheme = false) {
        RoundedCornersSurface(
            modifier = Modifier
                .height(148.dp)
                .fillMaxWidth(),
            topPadding = 48.dp,
        ) {

        }
    }
}