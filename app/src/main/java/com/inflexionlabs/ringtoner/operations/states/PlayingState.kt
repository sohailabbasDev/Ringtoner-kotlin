package com.inflexionlabs.ringtoner.operations.states

sealed class PlayingState{
    object Preparing : PlayingState()
    object Failed : PlayingState()
    object Playing : PlayingState()
//    object Finished : PlayingState()
    object Stopped : PlayingState()
    object Empty : PlayingState()
}
