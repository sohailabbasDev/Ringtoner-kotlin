package com.inflexionlabs.ringtoner.database.state

import com.inflexionlabs.ringtoner.database.model.Favourite

sealed class FavouriteEvent{
    data class AddFavourite(val favourite: Favourite) : FavouriteEvent()
    data class DeleteFavourite(val text: String) : FavouriteEvent()
//    data class IsFavourite(val text: String) : FavouriteEvent()

}
