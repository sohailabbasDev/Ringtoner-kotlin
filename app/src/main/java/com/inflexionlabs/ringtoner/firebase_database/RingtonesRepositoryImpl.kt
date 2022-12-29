package com.inflexionlabs.ringtoner.firebase_database

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.inflexionlabs.ringtoner.firebase_database.model.Category
import com.inflexionlabs.ringtoner.firebase_database.model.Ringtone
import com.inflexionlabs.ringtoner.firebase_database.repository.RingtonesRepository
import com.inflexionlabs.ringtoner.firebase_database.state.CategoriesListState
import com.inflexionlabs.ringtoner.firebase_database.state.RingtonesListState

class RingtonesRepositoryImpl : RingtonesRepository{

    private val firebaseFireStore = FirebaseFirestore.getInstance()
    private val collectionReference = firebaseFireStore.collection("Songs")
    private val categoryCollectionReference = firebaseFireStore.collection("Categories")

    override fun getAllCategories(result: (MutableState<CategoriesListState>) -> Unit) {
        val data : MutableList<Category> = mutableListOf()

        result.invoke(mutableStateOf(CategoriesListState.Loading))

        categoryCollectionReference.get().addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
            if (!queryDocumentSnapshots.isEmpty) {

                val list = queryDocumentSnapshots.documents

                for (snapshot in list) {
                    snapshot.toObject(Category::class.java)?.let { data.add(it) }
                }

                result.invoke(mutableStateOf(CategoriesListState.Successful(data)))

            }

//            Log.d("pat", "getAllCategories: do "+data[0])

        }.addOnFailureListener {
            result.invoke(mutableStateOf(CategoriesListState.Empty))

        }

    }

    override fun getAllRingtones(result: (List<Ringtone>) -> Unit) {
        val data = mutableListOf<Ringtone>()

//        result.invoke(mutableStateOf(RingtonesListState.Loading))
        collectionReference.get().addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
            if (!queryDocumentSnapshots.isEmpty) {

                val list = queryDocumentSnapshots.documents

                for (snapshot in list) {
                    snapshot.toObject(Ringtone::class.java)?.let { data.add(it) }
                }

                result.invoke(data)
//                result.invoke(mutableStateOf(RingtonesListState.Successful(data)))
//                Log.d("tag", "this is the data....  "+data.get(1).getText());
            }
        }.addOnFailureListener {
//            result.invoke(mutableStateOf(RingtonesListState.Empty))
        }
    }

    override fun getRingtonesByCategory(
        category: String,
        result: (MutableState<RingtonesListState>) -> Unit
    ) {

        val data = mutableListOf<Ringtone>()

        result.invoke(mutableStateOf(RingtonesListState.Loading))
        collectionReference.whereEqualTo("category" , category).get()
            .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
            if (!queryDocumentSnapshots.isEmpty) {

                val list = queryDocumentSnapshots.documents

                for (snapshot in list) {
                    snapshot.toObject(Ringtone::class.java)?.let { data.add(it) }
                }

                result.invoke(mutableStateOf(RingtonesListState.Successful(data)))
//                Log.d("tag", "this is the data....  "+data.get(1).getText());
            }
        }.addOnFailureListener {
            result.invoke(mutableStateOf(RingtonesListState.Empty))
        }

    }

}