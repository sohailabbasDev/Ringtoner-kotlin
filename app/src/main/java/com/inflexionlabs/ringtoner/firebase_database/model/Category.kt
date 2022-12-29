package com.inflexionlabs.ringtoner.firebase_database.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    var text: String? = null, val url: String? = null
) : Parcelable
