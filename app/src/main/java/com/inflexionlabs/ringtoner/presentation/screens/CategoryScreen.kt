package com.inflexionlabs.ringtoner.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.inflexionlabs.ringtoner.database.model.Favourite
import com.inflexionlabs.ringtoner.firebase_database.RingtonesViewModel
import com.inflexionlabs.ringtoner.firebase_database.model.Category
import com.inflexionlabs.ringtoner.firebase_database.state.RingtonesListState
import com.inflexionlabs.ringtoner.operations.RingtonePlayer
import com.inflexionlabs.ringtoner.operations.connectivity.ConnectivityObserver
import com.inflexionlabs.ringtoner.presentation.navigation.Routes
import com.inflexionlabs.ringtoner.presentation.screens.app_bar.MainTopBar
import com.inflexionlabs.ringtoner.presentation.screens.components.AddNetwork
import com.inflexionlabs.ringtoner.presentation.screens.components.Alerts
import com.inflexionlabs.ringtoner.presentation.screens.components.RingtoneCard
import com.inflexionlabs.ringtoner.presentation.screens.components.shimmer.ShimmerRingtoneCard
import com.inflexionlabs.ringtoner.presentation.screens.util.playClick

@ExperimentalFoundationApi
@Composable
fun CategoryScreen(networkStatus : ConnectivityObserver.Status, navHostController: NavHostController, viewModel: RingtonesViewModel = hiltViewModel()){

    val category = navHostController.previousBackStackEntry?.savedStateHandle?.get<Category>("category")

    viewModel.getRingtonesByCategory(category?.text.toString())

    val context = LocalContext.current

    try {
        if (RingtonePlayer.isPLaying()){
            RingtonePlayer.stopPlaying()
        }
    }catch (e : Exception){
        e.stackTrace
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = { MainTopBar(
        text = category?.text.toString(),
        spacing = 0,
        navigationBack = true,
        onBackPressed = {navHostController.popBackStack()}
    )} ) {it.calculateTopPadding()
        LazyColumn{

            stickyHeader {
                AddNetwork(networkStatus = networkStatus)
            }

            when(val ringtonesListState = viewModel.categoryRingtonesListState.value){
                is RingtonesListState.Empty -> {
                    item {
                        Box(modifier = Modifier
                            .fillParentMaxSize(), contentAlignment = Alignment.Center
                        ){
                            Text(text = "No data at the moment, please retry")
                        }
                    }

                }
                is RingtonesListState.Loading -> {
                    items(16){
                        ShimmerRingtoneCard()
                    }
                }
                is RingtonesListState.Successful -> {
                    items(ringtonesListState.ringtones.size){num ->
                        RingtoneCard(
                            ringtone = ringtonesListState.ringtones[num],
                            onArrowClick = {navHostController.currentBackStackEntry?.
                                savedStateHandle?.set(key = "ringtone", value = ringtonesListState.ringtones[num])
                                navHostController.navigate(Routes.Player.route)},
                            onPlayClick = {
                                if (networkStatus is ConnectivityObserver.Status.Available){
                                    playClick(num, ringtonesListState.ringtones[num].uri.toString(),
                                        ringtonesListState.ringtones[num].name.toString())
                                }else{
                                    Alerts.networkAlert(context)
                                }
                            },
                            favourite = Favourite(ringtonesListState.ringtones[num].name.toString(), ringtonesListState.ringtones[num].uri.toString()),
                            isForFavouriteScreen = false,
                            selected = num
                        )
                    }
                }
            }
        }
    }
}