package com.inflexionlabs.ringtoner.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.inflexionlabs.ringtoner.database.model.Favourite

@Database(entities = [Favourite::class], version = 1)
abstract class FavouriteDatabase : RoomDatabase(){
    abstract val favouriteDao : FavouriteDao

    companion object{
        const val DATABASE_NAME = "favourites_db"
    }
}