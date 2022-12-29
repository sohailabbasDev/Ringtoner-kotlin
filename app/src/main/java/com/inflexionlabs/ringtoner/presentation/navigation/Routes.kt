package com.inflexionlabs.ringtoner.presentation.navigation

sealed class Routes(val route : String){
    object Category : Routes("CATEGORY")
    object Details : Routes("DETAILS")
    object Player : Routes("PLAYER")
}