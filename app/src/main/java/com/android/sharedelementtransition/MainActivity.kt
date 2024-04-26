package com.android.sharedelementtransition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.android.sharedelementtransition.models.PlayBackData
import com.android.sharedelementtransition.ui.theme.SharedElementTransitionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SharedElementTransitionTheme(darkTheme = false) {
                SharedElementTransition()
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedElementTransition() {
    SharedTransitionLayout {
        val albums = PlayBackData().albums1
        val boundsTransform = { _: Rect, _: Rect -> tween<Rect>(500) }

        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = "preview") {
            composable("preview") {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(albums) { index, item ->
                        Box(modifier = Modifier
                            .sharedElement(
                                rememberSharedContentState(key = "image-$index"),
                                animatedVisibilityScope = this@composable,
                                boundsTransform = boundsTransform
                            )
                            .clickable {
                                navController.navigate("details/$index")
                            }
                        ) {
                            Image(
                                painter = painterResource(id = item.cover),
                                contentDescription = "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(10.dp))
                            )
                        }
                    }
                }
            }

            composable(
                "details/{item}",
                arguments = listOf(navArgument("item") { type = NavType.IntType })
            ) { navBackStackEntry ->
                val albumId = navBackStackEntry.arguments?.getInt("item")
                val album = albums[albumId!!]

                Column(modifier = Modifier
                    .background(Color.Blue)
                    .sharedElement(
                        rememberSharedContentState(key = "image-$albumId"),
                        animatedVisibilityScope = this@composable,
                    )
                    .fillMaxSize()
                    .clickable { navController.navigate("preview") }
                ) {
                    Image(
                        painterResource(id = album.cover),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(500.dp)
                    )
                    Text(text = album.author, fontSize = 20.sp, color = Color.White)
                }
            }
        }
    }
}

