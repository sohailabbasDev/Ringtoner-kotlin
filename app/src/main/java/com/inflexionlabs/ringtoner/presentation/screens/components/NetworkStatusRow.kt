package com.inflexionlabs.ringtoner.presentation.screens.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.inflexionlabs.ringtoner.operations.connectivity.ConnectivityObserver
import com.inflexionlabs.ringtoner.ui.theme.NetGreenColor
import com.inflexionlabs.ringtoner.ui.theme.TraceTextColor
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun NetworkStatusRow(text : String, color: Color){
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(22.dp)
        .background(color),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        Text(text = text, textAlign = TextAlign.Center, color = Color.White)
    }
}

@Composable
fun AddNetwork(networkStatus: ConnectivityObserver.Status){

    var isStatusVisible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = networkStatus){
        isStatusVisible = if (networkStatus is ConnectivityObserver.Status.Available){
            delay(3.seconds)
            false
        }else{
            true
        }
    }

    when(networkStatus){
        is ConnectivityObserver.Status.Unavailable -> {
            AnimatedVisibility(visible = true) {
                NetworkStatusRow(text = "No connection", color = Color.Red)
            }
        }
        is ConnectivityObserver.Status.Available -> {
            AnimatedVisibility(visible = isStatusVisible) {
                NetworkStatusRow(text = "Connected", color = NetGreenColor)
            }
        }
        is ConnectivityObserver.Status.Losing -> {
            AnimatedVisibility(visible = true) {
                NetworkStatusRow(text = "Losing connection", color = TraceTextColor)
            }
        }
        is ConnectivityObserver.Status.Lost -> {
            AnimatedVisibility(visible = true) {
                NetworkStatusRow(text = "Connection lost", Color.Red)
            }
        }
    }
}