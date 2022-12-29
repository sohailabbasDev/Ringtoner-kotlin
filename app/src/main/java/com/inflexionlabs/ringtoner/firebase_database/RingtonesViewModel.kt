package com.inflexionlabs.ringtoner.firebase_database

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inflexionlabs.ringtoner.firebase_database.repository.RingtonesRepository
import com.inflexionlabs.ringtoner.firebase_database.state.CategoriesListState
import com.inflexionlabs.ringtoner.firebase_database.state.RingtonesListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RingtonesViewModel @Inject constructor(private val repository: RingtonesRepository) : ViewModel() {
    
    private val _ringtonesListState : MutableState<RingtonesListState> = mutableStateOf(RingtonesListState.Empty)
    val ringtonesListState : State<RingtonesListState> = _ringtonesListState

    private val _categoryRingtonesListState : MutableState<RingtonesListState> = mutableStateOf(RingtonesListState.Empty)
    val categoryRingtonesListState : State<RingtonesListState> = _categoryRingtonesListState

    private val _categoryListState : MutableState<CategoriesListState> = mutableStateOf(CategoriesListState.Empty)
    val categoriesListState : State<CategoriesListState> = _categoryListState

    private val _searchList : MutableState<RingtonesListState> = mutableStateOf(RingtonesListState.Empty)
    val searchList : State<RingtonesListState> = _searchList

    private val _searchTextState: MutableState<String> = mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState

    companion object{
        val selectedOne : MutableState<Int> = mutableStateOf(-2)
        val isLoading = MutableStateFlow(true)
    }

    init {
        getAllCategories()
        getAllRingtones()
        isLoading.value = false
    }

    fun updateSearchTextState(newValue: String) {
        _searchTextState.value = newValue

        try {
            _searchList.value = RingtonesListState.Loading
            val listToSearch = (_ringtonesListState.value as RingtonesListState.Successful).ringtones
            if (newValue.isNotEmpty()){
                val result = listToSearch.filter {
                    it.name!!.contains(newValue, ignoreCase = true)
                }
                if (result.isEmpty()){
                    _searchList.value = RingtonesListState.Empty
                }else{
                    _searchList.value = RingtonesListState.Successful(result)
                }
            }else{
                _searchList.value = RingtonesListState.Successful(listToSearch)
            }
        }catch (e : Exception){
            e.stackTrace
            _searchList.value = RingtonesListState.Empty
        }

    }

    private fun getAllCategories(){
        _categoryListState.value = CategoriesListState.Loading

        viewModelScope.launch {
            repository.getAllCategories { _categoryListState.value = it.value }
        }
    }

    private fun getAllRingtones(){
        _ringtonesListState.value = RingtonesListState.Loading
        _searchList.value = RingtonesListState.Loading

        viewModelScope.launch {
            repository.getAllRingtones {
                _ringtonesListState.value = RingtonesListState.Successful(it)
                _searchList.value = RingtonesListState.Successful(it)
            }
        }

    }

    fun getRingtonesByCategory(category: String){
        _categoryRingtonesListState.value = RingtonesListState.Loading

        viewModelScope.launch {
            repository.getRingtonesByCategory(category){ _categoryRingtonesListState.value = it.value }
        }
    }

}