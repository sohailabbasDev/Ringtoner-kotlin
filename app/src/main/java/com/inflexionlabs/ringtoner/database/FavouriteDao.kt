package com.inflexionlabs.ringtoner.database

import androidx.room.*
import com.inflexionlabs.ringtoner.database.model.Favourite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteDao {

    @Query("SELECT * FROM favourite")
    fun getFavourites(): Flow<List<Favourite>>

//    @Query("SELECT text FROM favourite")
//    suspend fun getFavouritesNames() : List<String>

//    @Query("SELECT EXISTS(SELECT * FROM favourite WHERE text =(:text))")
//    suspend fun isFavourite(text : String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(favourite: Favourite)

    @Query("DELETE FROM favourite WHERE text = :text")
    suspend fun deleteFavourite(text: String)

}