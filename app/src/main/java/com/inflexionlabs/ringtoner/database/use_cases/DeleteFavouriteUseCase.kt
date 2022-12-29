package com.inflexionlabs.ringtoner.database.use_cases

import com.inflexionlabs.ringtoner.database.repository.FavouriteRepository

class DeleteFavouriteUseCase(private val repository: FavouriteRepository) {

    suspend operator fun invoke(text: String) {
        repository.deleteFavourite(text)
    }
}