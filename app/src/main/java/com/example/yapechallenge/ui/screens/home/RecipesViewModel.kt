package com.example.yapechallenge.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yapechallenge.core.domain.Recipe
import com.example.yapechallenge.core.usecases.GetAllRecipes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(private val getAllRecipes: GetAllRecipes) : ViewModel() {

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: Flow<List<Recipe>> = _recipes

    private val _isLoading = MutableStateFlow(true)
    val isLoading: Flow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            getAllRecipes.buildUseCase(Unit).collect {
                _recipes.emit(it)
                _isLoading.emit(false)
            }
        }
    }

}