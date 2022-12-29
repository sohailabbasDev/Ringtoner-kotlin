package com.inflexionlabs.ringtoner.presentation.screens.components.animations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.inflexionlabs.ringtoner.R

@Composable
fun AboutPageAnim(){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.aboutanim))
    val progress by animateLottieCompositionAsState(composition)
    LottieAnimation(
        composition = composition,
        progress = { progress },
    )
}