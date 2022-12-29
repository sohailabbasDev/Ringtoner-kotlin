package com.inflexionlabs.ringtoner.firebase_database.state

import com.inflexionlabs.ringtoner.firebase_database.model.Category

sealed class CategoriesListState {
    object Loading : CategoriesListState()
    object Empty : CategoriesListState()
    data class Successful(val categories : List<Category> ) : CategoriesListState()
}
