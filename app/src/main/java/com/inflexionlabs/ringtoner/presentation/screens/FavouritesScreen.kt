package com.inflexionlabs.ringtoner.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import com.inflexionlabs.ringtoner.firebase_database.model.Ringtone
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.inflexionlabs.ringtoner.database.FavouritesViewModel
import com.inflexionlabs.ringtoner.operations.RingtonePlayer
import com.inflexionlabs.ringtoner.operations.connectivity.ConnectivityObserver
import com.inflexionlabs.ringtoner.presentation.navigation.Routes
import com.inflexionlabs.ringtoner.presentation.screens.app_bar.MainTopBar
import com.inflexionlabs.ringtoner.presentation.screens.components.AddNetwork
import com.inflexionlabs.ringtoner.presentation.screens.components.Alerts
import com.inflexionlabs.ringtoner.presentation.screens.components.animations.NoFavouritesAnim
import com.inflexionlabs.ringtoner.presentation.screens.components.RingtoneCard
import com.inflexionlabs.ringtoner.presentation.screens.util.playClick

@ExperimentalFoundationApi
@Composable
fun FavouriteScreen(networkStatus :  ConnectivityObserver.Status, navHostController: NavHostController, viewModel: FavouritesViewModel = hiltViewModel(), lazyListState: LazyListState){

    val state = viewModel.favourites.value

    try {
        if (RingtonePlayer.isPLaying()){
            RingtonePlayer.stopPlaying()
        }
    }catch (e : Exception){
        e.stackTrace
    }

    val context = LocalContext.current

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = { MainTopBar(
        text = "Favourites",
        spacing = 0,
        navigationBack = false,
        onBackPressed = {}
    )}) {it.calculateTopPadding()
        LazyColumn(modifier = Modifier.fillMaxSize(), state = lazyListState){

            stickyHeader {
                AddNetwork(networkStatus = networkStatus)
            }

            if (state.favourites.isEmpty()){
                item {
                    Column(modifier = Modifier
                        .fillParentMaxWidth()
                        .fillParentMaxHeight(0.8f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally){
                        NoFavouritesAnim()
                        Text(
                            text = "No favourites yet!",
                            color = MaterialTheme.colors.surface,
                            fontSize = 26.sp
                        )
                    }
                }
            }else{
                items(state.favourites.size){num ->
                    RingtoneCard(
                        ringtone = null,
                        onArrowClick = { navHostController.currentBackStackEntry?.
                        savedStateHandle?.set(key = "ringtone",
                            value = Ringtone(name = state.favourites[num].text, uri = state.favourites[num].url))
                            navHostController.navigate(Routes.Player.route) },
                        onPlayClick = {
                            if (networkStatus is ConnectivityObserver.Status.Available){
                                playClick(num, state.favourites[num].url, state.favourites[num].text)

                            }else{
                                Alerts.networkAlert(context)
                            }
                        },
                        viewModel = viewModel,
                        favourite = state.favourites[num],
                        isForFavouriteScreen = true,
                        selected = num
                    )
                }
            }
        }
    }
}