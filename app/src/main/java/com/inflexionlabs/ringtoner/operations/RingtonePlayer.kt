package com.inflexionlabs.ringtoner.operations

import android.media.MediaPlayer
import android.os.Environment
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.inflexionlabs.ringtoner.operations.states.PlayingState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File

object RingtonePlayer {
    private lateinit var mediaPlayer: MediaPlayer

    private val playingState : MutableState<PlayingState> = mutableStateOf(PlayingState.Empty)
    val playingStatePub : State<PlayingState> = playingState

    private val mediaJob = Job()

    private val mediaScope = CoroutineScope(Dispatchers.Main + mediaJob)

    fun makePlayer(){
        mediaPlayer = MediaPlayer()
    }

    fun playRingtone(url: String, name: String) = mediaScope.launch(Dispatchers.IO){

        if (Downloader.downloadExists(name)){
            try {
                val songFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),
                    "$name.mp3"
                )
                playingState.value = PlayingState.Preparing
                mediaPlayer.reset()
                mediaPlayer.setDataSource(songFile.toString())
                mediaPlayer.prepareAsync()
            }catch (e : Exception){
                playingState.value = PlayingState.Failed
            }
        }else{
            try {
                playingState.value = PlayingState.Preparing
                mediaPlayer.reset()
                mediaPlayer.setDataSource(url)
                mediaPlayer.prepareAsync()
            }catch (e : Exception){
                playingState.value = PlayingState.Failed
            }
        }

        mediaPlayer.setOnPreparedListener {
            mediaPlayer.start()
            playingState.value = PlayingState.Playing
        }
        mediaPlayer.setOnCompletionListener {
            playingState.value = PlayingState.Stopped
        }
    }

    fun isPLaying() : Boolean{
        return mediaPlayer.isPlaying
    }


    fun reset(){
        mediaPlayer.reset()
        playingState.value = PlayingState.Empty
    }

    fun stopPlaying(){
        if (mediaPlayer.isPlaying){
            mediaPlayer.stop()
            playingState.value = PlayingState.Stopped
        }
    }

    fun destroyPlayer(){
        mediaPlayer.release()
    }
}