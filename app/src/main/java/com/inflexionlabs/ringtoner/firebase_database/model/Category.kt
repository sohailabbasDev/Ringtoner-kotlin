package com.inflexionlabs.ringtoner.firebase_database.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class Category(
    var text: String? = null, val url: String? = null
) : Parcelable
