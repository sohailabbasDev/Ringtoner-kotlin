package com.inflexionlabs.ringtoner.database.state

import com.inflexionlabs.ringtoner.database.model.Favourite

data class FavouriteState(var favourites : List<Favourite> = emptyList())
