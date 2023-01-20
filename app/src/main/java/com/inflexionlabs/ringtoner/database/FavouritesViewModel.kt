package com.inflexionlabs.ringtoner.database

import android.util.Log
import androidx.annotation.Keep
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inflexionlabs.ringtoner.database.state.FavouriteEvent
import com.inflexionlabs.ringtoner.database.state.FavouriteState
import com.inflexionlabs.ringtoner.database.use_cases.FavouriteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@Keep
class FavouritesViewModel @Inject constructor(private val favouriteUseCase: FavouriteUseCases) : ViewModel(){

    private val _favourites = mutableStateOf(FavouriteState())
    val favourites : State<FavouriteState> = _favourites

    private var getFavouritesJob : Job? = null

    init {
        getFavourites()
    }

    companion object{
        val isFavouriteList = arrayListOf<String>()
    }

    fun onEvent(favouriteEvent: FavouriteEvent) {

        when(favouriteEvent){
            is FavouriteEvent.AddFavourite -> {
                viewModelScope.launch {
                    favouriteUseCase.addFavouriteUseCase(favouriteEvent.favourite)
                }
            }
            is FavouriteEvent.DeleteFavourite -> {
                viewModelScope.launch {
                    favouriteUseCase.deleteFavouriteUseCase(favouriteEvent.text)
                }
            }
        }
    }


    private fun getFavourites(){
        getFavouritesJob?.cancel()
        getFavouritesJob = favouriteUseCase.getFavouritesUseCase().onEach {
            _favourites.value = favourites.value.copy( favourites = it)
            Log.d("fav", "getFavourites: ")
        }.launchIn(viewModelScope)
    }

}