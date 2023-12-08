package com.example.yapechallenge.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yapechallenge.core.domain.Recipe
import com.example.yapechallenge.core.usecases.GetRecipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(private val getRecipeUseCase: GetRecipe) :
    ViewModel() {

    /*private val localDataSource = LocalDataSourceImpl
    private val remoteDataSource = RemoteDataSourceImpl(
        KtorClientImpl()
    )
    private val repository = RecipeRepositoryImpl(remoteDataSource, localDataSource, Dispatchers.IO)*/

    private val _recipe = MutableStateFlow(Recipe.EMPTY)
    val recipe: Flow<Recipe> = _recipe

    private val _isLoading = MutableStateFlow(true)
    val isLoading: Flow<Boolean> = _isLoading

    fun getRecipeDetail(recipeId: Int) {
        viewModelScope.launch {
            if (_recipe.value == Recipe.EMPTY) {
                val result = getRecipeUseCase.buildUseCase(GetRecipe.Params(recipeId))
                _recipe.emit(result)
                _isLoading.emit(false)
            }
        }
    }

}