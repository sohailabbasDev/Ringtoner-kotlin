package com.inflexionlabs.ringtoner.database.use_cases

import com.inflexionlabs.ringtoner.database.model.Favourite
import com.inflexionlabs.ringtoner.database.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow

class GetFavouritesUseCase (private val repository : FavouriteRepository){

    operator fun invoke() : Flow<List<Favourite>> {
        return repository.getFavourites()
    }
}