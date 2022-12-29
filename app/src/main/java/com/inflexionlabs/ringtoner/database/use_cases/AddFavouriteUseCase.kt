package com.inflexionlabs.ringtoner.database.use_cases

import com.inflexionlabs.ringtoner.database.model.Favourite
import com.inflexionlabs.ringtoner.database.repository.FavouriteRepository

class AddFavouriteUseCase(private val repository: FavouriteRepository) {

    suspend operator fun invoke(favourite: Favourite){
        repository.insertFavourite(favourite)
    }

}