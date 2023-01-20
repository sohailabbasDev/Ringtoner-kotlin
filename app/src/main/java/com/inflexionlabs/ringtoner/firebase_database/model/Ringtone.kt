package com.inflexionlabs.ringtoner.firebase_database.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class Ringtone(
    var category: String? = null,
    val name: String? = null,
    val uri: String? = null
) : Parcelable
