package com.inflexionlabs.ringtoner.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.inflexionlabs.ringtoner.database.model.Favourite
import com.inflexionlabs.ringtoner.firebase_database.RingtonesViewModel
import com.inflexionlabs.ringtoner.firebase_database.state.CategoriesListState
import com.inflexionlabs.ringtoner.firebase_database.state.RingtonesListState
import com.inflexionlabs.ringtoner.operations.RingtonePlayer
import com.inflexionlabs.ringtoner.operations.connectivity.ConnectivityObserver
import com.inflexionlabs.ringtoner.presentation.navigation.Routes
import com.inflexionlabs.ringtoner.presentation.screens.app_bar.MainTopBar
import com.inflexionlabs.ringtoner.presentation.screens.components.*
import com.inflexionlabs.ringtoner.presentation.screens.components.animations.NoInternetAnim
import com.inflexionlabs.ringtoner.presentation.screens.components.shimmer.ShimmerRingtoneCard
import com.inflexionlabs.ringtoner.presentation.screens.util.playClick

@ExperimentalFoundationApi
@Composable
fun HomeScreen(networkStatus: ConnectivityObserver.Status,
               navHostController: NavHostController,
               viewModel: RingtonesViewModel = hiltViewModel(),
               lazyListState: LazyListState){

    val context = LocalContext.current

    try {
        if (RingtonePlayer.isPLaying()){
            RingtonePlayer.stopPlaying()
        }
    }catch (e : Exception){
        e.stackTrace
    }

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = { MainTopBar(
        text = "RINGTONER",
        spacing = 12,
        navigationBack = false,
        onBackPressed = {}
    ) }) {it.calculateTopPadding()

        LazyColumn(modifier = Modifier.fillMaxSize(), state = lazyListState) {

            stickyHeader {
                AddNetwork(networkStatus = networkStatus)
            }

            when(val categoryListState = viewModel.categoriesListState.value){
                is CategoriesListState.Empty -> {
                    item {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .fillParentMaxHeight(0.3f), contentAlignment = Alignment.Center
                        ){
                            Text(text = "No data at the moment, please retry")
                        }
                    }
                }
                is CategoriesListState.Loading -> {
                    if (networkStatus is ConnectivityObserver.Status.Available){
                        item {
                            LazyRow{
                                items(8){
                                    ShimmerCategoryCard()
                                }
                            }
                        }
                    }
                }
                is CategoriesListState.Successful -> {
                    item {
                        LazyRow{
                            items(categoryListState.categories){category ->
                                CategoryCard(
                                    modifier = Modifier
                                        .clickable {
                                            navHostController.currentBackStackEntry?.savedStateHandle?.set(
                                                key = "category",
                                                value = category
                                            )
                                            navHostController.navigate(Routes.Category.route)
                                        }
                                        .width(160.dp)
                                        .height(170.dp)
                                        .padding(top = 8.dp, bottom = 8.dp)
                                        .padding(6.dp), category = category
                                )
                            }
                        }
                    }
                }
            }

            when(val ringtonesListState = viewModel.ringtonesListState.value){
                is RingtonesListState.Empty -> {
                    item {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .fillParentMaxHeight(0.6f), contentAlignment = Alignment.Center
                        ){
                            Text(text = "No data at the moment, please retry")
                        }
                    }
                }
                is RingtonesListState.Loading -> {
                    if (networkStatus is ConnectivityObserver.Status.Available){
                        items(16){
                            ShimmerRingtoneCard()
                        }
                    }else{
                        item {
                            Column(modifier = Modifier
                                .fillParentMaxWidth()
                                .fillParentMaxHeight(0.8f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally){
                                NoInternetAnim()
                                Text(
                                    modifier = Modifier.padding(top = 12.dp),
                                    text = "No Internet found!",
                                    color = MaterialTheme.colors.surface,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }
                is RingtonesListState.Successful -> {
                    items(ringtonesListState.ringtones.size){num ->
                        RingtoneCard(
                            ringtone = ringtonesListState.ringtones[num],
                            onArrowClick = { navHostController.currentBackStackEntry?.
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
                            favourite = Favourite(ringtonesListState.ringtones[num].name.toString(),
                                                  ringtonesListState.ringtones[num].uri.toString()),
                            isForFavouriteScreen = false,
                            selected = num
                        )
                    }
                }
            }

        }
    }
}