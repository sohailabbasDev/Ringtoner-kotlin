package com.inflexionlabs.ringtoner.operations.states

sealed class RingtoneSetState {
    object Failed : RingtoneSetState()
    object Set : RingtoneSetState()
    //    object Finished : PlayingState()
    object Setting : RingtoneSetState()
    object Empty : RingtoneSetState()
}