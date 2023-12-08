package com.example.yapechallenge.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yapechallenge.core.domain.SearchRecipe
import com.example.yapechallenge.core.usecases.FindRecipe
import com.example.yapechallenge.core.usecases.GetSearchHistory
import com.example.yapechallenge.core.usecases.SaveSearchHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val findRecipe: FindRecipe,
    private val getSearchHistory: GetSearchHistory,
    private val saveSearchHistory: SaveSearchHistory
) : ViewModel() {

    private val _searchResults = MutableSharedFlow<List<SearchRecipe>>(replay = 1)
    val searchResults: Flow<List<SearchRecipe>> = _searchResults

    private val _searchHistory = MutableSharedFlow<List<String>>(replay = 1)
    val searchHistory: Flow<List<String>> = _searchHistory

    init {
        viewModelScope.launch {
            _searchHistory.emit(getSearchHistory.buildUseCase(Unit))
        }
    }

    fun onSearchClick(query: String) {
        viewModelScope.launch {
            saveSearchHistory.buildUseCase(SaveSearchHistory.Params(query))
            _searchResults.emit(findRecipe.buildUseCase(FindRecipe.Params(query)))
        }
    }

}