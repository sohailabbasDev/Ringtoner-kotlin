package com.inflexionlabs.ringtoner.firebase_database.state

import com.inflexionlabs.ringtoner.firebase_database.model.Ringtone

sealed class RingtonesListState{
    data class Successful(val ringtones : List<Ringtone> = emptyList()) : RingtonesListState()
    object Loading : RingtonesListState()
    object Empty : RingtonesListState()

//    object Failure : RingtonesListState()
}
