package com.inflexionlabs.ringtoner.presentation.bottom_bar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen(
        route = "home",
        title = "Home",
        icon = Icons.Default.Home
    )

    object Search : BottomBarScreen(
        route = "search",
        title = "Search",
        icon = Icons.Default.Search
    )

    object Favourites : BottomBarScreen(
        route = "favourites",
        title = "Favourites",
        icon = Icons.Default.Favorite
    )

    object About : BottomBarScreen(
        route = "about",
        title = "About",
        icon = Icons.Default.Menu
    )
}
