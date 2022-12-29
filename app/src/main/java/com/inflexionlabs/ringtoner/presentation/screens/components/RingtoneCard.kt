package com.inflexionlabs.ringtoner.presentation.screens.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inflexionlabs.ringtoner.database.FavouritesViewModel
import com.inflexionlabs.ringtoner.database.model.Favourite
import com.inflexionlabs.ringtoner.database.state.FavouriteEvent
import com.inflexionlabs.ringtoner.firebase_database.RingtonesViewModel
import com.inflexionlabs.ringtoner.firebase_database.model.Ringtone
import com.inflexionlabs.ringtoner.operations.RingtonePlayer
import com.inflexionlabs.ringtoner.operations.states.PlayingState

@Composable
fun RingtoneCard(ringtone: Ringtone? = null,
                 onArrowClick: () -> Unit,
                 onPlayClick : () -> Unit,
                 viewModel: FavouritesViewModel = hiltViewModel(),
                 favourite: Favourite,
                 isForFavouriteScreen: Boolean,
                 isForSearchScreen: Boolean = false,
                 selected : Int){

    val playCircle = Icons.Default.PlayCircle
    var isFavourite by remember { mutableStateOf(FavouritesViewModel.isFavouriteList.contains(favourite.text)) }

    Card(modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .padding(12.dp),
        elevation = 8.dp, backgroundColor = MaterialTheme.colors.background,
        shape = RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp, topEnd = 6.dp, bottomEnd = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(top = 6.dp, bottom = 6.dp),
                onClick = onPlayClick) {
                if (RingtonesViewModel.selectedOne.value == selected){
                    when(RingtonePlayer.playingStatePub.value) {
                        is PlayingState.Empty -> {
                            PlayIcon(icon = playCircle)
                        }
                        is PlayingState.Failed -> {
                            PlayIcon(icon = playCircle)
                        }
                        is PlayingState.Preparing -> {
                            CircularProgressIndicator(color = MaterialTheme.colors.surface)
                        }
                        is PlayingState.Stopped -> {
                            PlayIcon(icon = playCircle)
                        }
                        is PlayingState.Playing -> {
                            PlayIcon(icon = Icons.Default.StopCircle)
                        }
                    }
                }else{
                    PlayIcon(icon = playCircle)
                }

            }
            Text(modifier = Modifier
                .weight(3f)
                .padding(start = 16.dp),
                text = ringtone?.name ?: favourite.text,
                maxLines = 1,
                textAlign = TextAlign.Start, color = MaterialTheme.colors.surface,
                fontSize = 22.sp , fontStyle = MaterialTheme.typography.body2.fontStyle,
                fontWeight = MaterialTheme.typography.body2.fontWeight,
                fontFamily = MaterialTheme.typography.body2.fontFamily)
            if (!isForSearchScreen){
                IconButton(modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(14.dp),
                    onClick = {
                        if (!isForFavouriteScreen){
                            isFavourite = if (isFavourite){
                                viewModel.onEvent(FavouriteEvent.DeleteFavourite(favourite.text))
                                FavouritesViewModel.isFavouriteList.remove(favourite.text)
                                false
                            }else{
                                viewModel.onEvent(FavouriteEvent.AddFavourite(favourite))
                                FavouritesViewModel.isFavouriteList.add(favourite.text)
                                true
                            }
                        }else{
                            viewModel.onEvent(FavouriteEvent.DeleteFavourite(favourite.text))
                            FavouritesViewModel.isFavouriteList.remove(favourite.text)
                        }

                    }) {
                    if (!isForFavouriteScreen){
                        Icon(modifier = Modifier.fillMaxSize(),
                            imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            tint = if (isFavourite) Color.Red else MaterialTheme.colors.surface,
                            contentDescription = "Favourite button")
                    }else{
                        Icon(modifier = Modifier.fillMaxSize(),
                            imageVector = Icons.Default.Delete,
                            tint = Color.Red,
                            contentDescription = "Favourite button")
                    }
                }
            }
            IconButton(modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(14.dp),
                onClick = onArrowClick) {
                Icon(modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Default.ArrowForward,
                    tint = MaterialTheme.colors.surface,
                    contentDescription = "Play button")
            }
        }
    }
}

@Composable
fun PlayIcon(icon : ImageVector){
    Icon(
        modifier = Modifier.fillMaxSize(),
        imageVector = icon,
        tint = MaterialTheme.colors.surface,
        contentDescription = "Play/Stop button"
    )
}