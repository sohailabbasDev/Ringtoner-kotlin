package com.inflexionlabs.ringtoner.database

import com.inflexionlabs.ringtoner.database.model.Favourite
import com.inflexionlabs.ringtoner.database.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow

class FavouriteRepositoryImpl(private val dao: FavouriteDao) : FavouriteRepository {
    override fun getFavourites(): Flow<List<Favourite>> {
        return dao.getFavourites()
    }

    override suspend fun insertFavourite(favourite: Favourite) {
         dao.insertFavourite(favourite)
    }

    override suspend fun deleteFavourite(text: String) {
         dao.deleteFavourite(text)
    }
}