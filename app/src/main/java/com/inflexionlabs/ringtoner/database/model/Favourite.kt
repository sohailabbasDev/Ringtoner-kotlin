package com.inflexionlabs.ringtoner.database.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
@Keep
data class Favourite(
    val text : String,
    val url : String,
    @PrimaryKey val id : Int? = null
)
