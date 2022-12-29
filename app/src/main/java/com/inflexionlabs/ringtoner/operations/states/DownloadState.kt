package com.inflexionlabs.ringtoner.operations.states

sealed class DownloadState{
    object Preparing : DownloadState()
    object Started : DownloadState()
//    object Finished : DownloadState()
    object Downloaded : DownloadState()
    object Failed : DownloadState()
    object Empty : DownloadState()
}