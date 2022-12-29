package com.inflexionlabs.ringtoner.presentation.screens.components.animations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.airbnb.lottie.compose.*
import com.inflexionlabs.ringtoner.R

@Composable
fun NoInternetAnim(){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.wifiloading))
    val progress by animateLottieCompositionAsState(composition, isPlaying = true, iterations = LottieConstants.IterateForever)
    LottieAnimation(
        composition = composition,
        progress = { progress }
    )
}