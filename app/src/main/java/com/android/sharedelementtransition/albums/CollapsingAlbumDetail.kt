package com.android.sharedelementtransition.albums

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.sharedelementtransition.R
import com.android.sharedelementtransition.models.HeaderParams
import com.android.sharedelementtransition.statusBarsPaddingWithOffset
import com.android.sharedelementtransition.ui.TopMenu

const val TOP_MENU_TITLE1 = "Now Playing"

class CollapsingHeaderState(topInset: Dp) {
    val headerMaxHeight = topInset + MAX_HEADER_HEIGHT
    var headerHeight: Dp by mutableStateOf(headerMaxHeight)

    companion object{
        val MAX_HEADER_HEIGHT = 450.dp
    }
}

@Composable
@Stable
fun rememberCollapsingHeaderState(key: Any = Unit, topInset: Dp) = remember(key1 = key) {
    CollapsingHeaderState(topInset = topInset)
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    params: HeaderParams,
    isAppearing: Boolean,
    contentAlphaProvider: State<Float>,
    backgroundColorProvider: State<Color>,
    elevationProvider: () -> Dp = { 0.dp },
    onBackClick: () -> Unit = {},
) {
    SharedElementContainer(
        modifier = modifier
            .fillMaxSize()
            .shadow(elevationProvider())
            .background(backgroundColorProvider.value),
        params = params.sharedElementParams,
        isForward = isAppearing,
        title = {
            HeaderTitle(
                alphaProvider = contentAlphaProvider,
                onBackClick = onBackClick
            )
        },
        labels = {
            Labels(
                title = params.title,
                author = params.author,
                alphaProvider = contentAlphaProvider
            )
        },
        sharedElement = {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1f),
                painter = painterResource(id = params.coverId),
                contentDescription = "",
            )
        }
    )
}

@Composable
private fun BoxScope.HeaderTitle(
    alphaProvider: State<Float>,
    onBackClick: () -> Unit,
) {
    TopMenu(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .graphicsLayer { alpha = alphaProvider.value }
            .align(Alignment.TopCenter)
            .statusBarsPaddingWithOffset(),
        title = TOP_MENU_TITLE1,
        iconsTint = MaterialTheme.colors.onSurface,
        endIconResId = R.drawable.baseline_share_24,
        onStartIconClick = onBackClick
    )
}

@Composable
private fun BoxScope.Labels(
    title: String, 
    author: String,
    alphaProvider: State<Float>,
) {
    Column(  
        modifier = Modifier
            .align(Alignment.Center)
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            textAlign = TextAlign.Center,
            modifier = Modifier.graphicsLayer { alpha = alphaProvider.value },
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface,
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = author,
            modifier = Modifier.graphicsLayer { alpha = alphaProvider.value },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface,
        )
    }
}