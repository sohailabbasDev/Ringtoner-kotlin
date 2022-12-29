package com.inflexionlabs.ringtoner.presentation.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.inflexionlabs.ringtoner.operations.connectivity.ConnectivityObserver
import com.inflexionlabs.ringtoner.presentation.bottom_bar.BottomBarScreen
import com.inflexionlabs.ringtoner.presentation.screens.*

@ExperimentalFoundationApi
@ExperimentalPermissionsApi
@Composable
fun HomeNavGraph(navController: NavHostController, scrollState : LazyListState, networkStatus: ConnectivityObserver.Status){

    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route)
    {
        composable(route = BottomBarScreen.Home.route){
            HomeScreen(navHostController = navController, lazyListState = scrollState, networkStatus = networkStatus)
        }
        composable(route = BottomBarScreen.Search.route){
            SearchScreen(navHostController = navController, lazyListState = scrollState, networkStatus = networkStatus)
        }
        composable(route = BottomBarScreen.Favourites.route){
            FavouriteScreen(navHostController = navController, lazyListState = scrollState, networkStatus = networkStatus)
        }
        composable(route = BottomBarScreen.About.route){
            AboutScreen(navController)
        }

        composable(route = Routes.Details.route){
            DetailsScreen(navHostController = navController)
        }

        composable(route = Routes.Category.route){
            CategoryScreen(navHostController = navController, networkStatus = networkStatus)
        }

        composable(route = Routes.Player.route){
            PlayScreen(navHostController = navController, networkStatus = networkStatus)
        }
    }

}