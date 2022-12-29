package com.inflexionlabs.ringtoner.firebase_database.repository

import androidx.compose.runtime.MutableState
import com.inflexionlabs.ringtoner.firebase_database.model.Ringtone
import com.inflexionlabs.ringtoner.firebase_database.state.CategoriesListState
import com.inflexionlabs.ringtoner.firebase_database.state.RingtonesListState

interface RingtonesRepository {

    fun getAllCategories(result: (MutableState<CategoriesListState>) -> Unit)

    fun getAllRingtones(result: (List<Ringtone>) -> Unit)

    fun getRingtonesByCategory(category: String, result: (MutableState<RingtonesListState>) -> Unit)

}