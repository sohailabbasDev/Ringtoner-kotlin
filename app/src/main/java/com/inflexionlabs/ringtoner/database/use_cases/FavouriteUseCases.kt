package com.inflexionlabs.ringtoner.database.use_cases

data class FavouriteUseCases(
    val getFavouritesUseCase: GetFavouritesUseCase,
    val deleteFavouriteUseCase: DeleteFavouriteUseCase,
    val addFavouriteUseCase: AddFavouriteUseCase,
)
