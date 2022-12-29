package com.inflexionlabs.ringtoner.presentation.screens.util

import com.inflexionlabs.ringtoner.firebase_database.RingtonesViewModel
import com.inflexionlabs.ringtoner.operations.states.PlayingState
import com.inflexionlabs.ringtoner.operations.RingtonePlayer

fun playClick(num : Int, url : String, name: String){

    if (RingtonePlayer.isPLaying()){
        if (RingtonesViewModel.selectedOne.value == num){
            RingtonePlayer.stopPlaying()
//                                        Log.d("ffh", "HomeScreen: ffh")
            return
        }
        RingtonePlayer.stopPlaying()
        RingtonePlayer.playRingtone(url, name)
        RingtonesViewModel.selectedOne.value = num
    }else{

        when(RingtonePlayer.playingStatePub.value){
            is PlayingState.Stopped -> {
                RingtonePlayer.playRingtone(url, name)
                RingtonesViewModel.selectedOne.value = num
            }
            is PlayingState.Preparing -> {

            }
            is PlayingState.Playing -> {
                RingtonePlayer.stopPlaying()
            }
            is PlayingState.Failed -> {
                RingtonePlayer.stopPlaying()
            }
            is PlayingState.Empty -> {
                RingtonePlayer.playRingtone(url, name)
                RingtonesViewModel.selectedOne.value = num
            }
        }

    }
}