package com.inflexionlabs.ringtoner.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.inflexionlabs.ringtoner.database.model.Favourite
import com.inflexionlabs.ringtoner.firebase_database.RingtonesViewModel
import com.inflexionlabs.ringtoner.firebase_database.state.RingtonesListState
import com.inflexionlabs.ringtoner.operations.RingtonePlayer
import com.inflexionlabs.ringtoner.operations.connectivity.ConnectivityObserver
import com.inflexionlabs.ringtoner.presentation.navigation.Routes
import com.inflexionlabs.ringtoner.presentation.screens.components.AddNetwork
import com.inflexionlabs.ringtoner.presentation.screens.components.Alerts
import com.inflexionlabs.ringtoner.presentation.screens.components.RingtoneCard
import com.inflexionlabs.ringtoner.presentation.screens.components.animations.NoSearchDataAnim
import com.inflexionlabs.ringtoner.presentation.screens.components.shimmer.ShimmerRingtoneCard
import com.inflexionlabs.ringtoner.presentation.screens.util.playClick

@ExperimentalFoundationApi
@Composable
fun SearchScreen(networkStatus :  ConnectivityObserver.Status, navHostController: NavHostController, viewModel: RingtonesViewModel = hiltViewModel(), lazyListState: LazyListState){

    val searchText by viewModel.searchTextState

    try {
        if (RingtonePlayer.isPLaying()){
            RingtonePlayer.stopPlaying()
        }
    }catch (e : Exception){
        e.stackTrace
    }

    val context = LocalContext.current

    Scaffold(topBar = {
        SearchBar(
        text = searchText,
        onTextChange = {
            viewModel.updateSearchTextState(it)
            if (RingtonePlayer.isPLaying()){
                RingtonePlayer.reset()
            }
        },
        onSearchClicked = {}
    )
    }) {it.calculateTopPadding()
        LazyColumn(modifier = Modifier.fillMaxSize(), state = lazyListState){

            stickyHeader {
                AddNetwork(networkStatus = networkStatus)
            }

            when(val ringtonesListState = viewModel.searchList.value){
                is RingtonesListState.Empty -> {

                    item {
                        Column(modifier = Modifier
                            .fillParentMaxWidth()
                            .fillParentMaxHeight(0.8f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally){
                            Box(modifier = Modifier.size(280.dp)){
                                NoSearchDataAnim()
                            }
                            Text(
                                text = "No data found",
                                color = MaterialTheme.colors.surface,
                                fontSize = 26.sp
                            )
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
                            isForSearchScreen = true,
                            selected = num
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(text: String,
              onTextChange: (String) -> Unit,
              onSearchClicked: (String) -> Unit){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.primary
    ) {
        TextField(modifier = Modifier
            .fillMaxWidth(),
            value = text,
            onValueChange = {
                onTextChange(it)
            },
            placeholder = {
                Text(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    text = "Search here...",
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.White
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = Color.White
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = Color.White.copy(alpha = ContentAlpha.medium)
            ))
    }
}