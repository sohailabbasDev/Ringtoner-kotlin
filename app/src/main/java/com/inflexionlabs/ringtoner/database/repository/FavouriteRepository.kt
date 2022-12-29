package com.inflexionlabs.ringtoner.database.repository

import com.inflexionlabs.ringtoner.database.model.Favourite
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {
    fun getFavourites(): Flow<List<Favourite>>

    suspend fun insertFavourite(favourite: Favourite)

    suspend fun deleteFavourite(text: String)
}