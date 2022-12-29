package com.inflexionlabs.ringtoner.operations.connectivity

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {

    fun observe(): Flow<Status>

    sealed class Status{
        object Available : Status()
        object Unavailable : Status()
        object Losing : Status()
        object Lost : Status()
    }
//    enum class Status {
//        Available, Unavailable, Losing, Lost
//    }
}