package com.inflexionlabs.ringtoner.di

import android.app.Application
import androidx.room.Room
import com.inflexionlabs.ringtoner.database.FavouriteDatabase
import com.inflexionlabs.ringtoner.database.FavouriteRepositoryImpl
import com.inflexionlabs.ringtoner.database.repository.FavouriteRepository
import com.inflexionlabs.ringtoner.database.use_cases.*
import com.inflexionlabs.ringtoner.firebase_database.RingtonesRepositoryImpl
import com.inflexionlabs.ringtoner.firebase_database.repository.RingtonesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFavouritesDatabase(app : Application) : FavouriteDatabase{
        return Room.databaseBuilder(
            app, FavouriteDatabase::class.java,
            FavouriteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavouriteRepository(db: FavouriteDatabase): FavouriteRepository {
        return FavouriteRepositoryImpl(db.favouriteDao)
    }

    @Provides
    @Singleton
    fun provideFavouriteUseCases(repository: FavouriteRepository) : FavouriteUseCases{
        return FavouriteUseCases(
            getFavouritesUseCase = GetFavouritesUseCase(repository),
            deleteFavouriteUseCase = DeleteFavouriteUseCase(repository),
            addFavouriteUseCase = AddFavouriteUseCase(repository),
        )
    }

    @Provides
    @Singleton
    fun provideRingtoneRepository() : RingtonesRepository{
        return RingtonesRepositoryImpl()
    }

}