package com.example.yapechallenge.core.data

import com.example.yapechallenge.core.domain.Recipe
import com.example.yapechallenge.core.domain.SearchRecipe
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun getRecipe(id: Int): Recipe
    suspend fun getAllRecipes(): Flow<List<Recipe>>
    suspend fun getAllSearchRecipes(): Flow<List<SearchRecipe>>
    suspend fun saveRecipes(recipes: List<Recipe>)
    suspend fun saveSearchRecipe(recipes: List<SearchRecipe>)
    suspend fun saveRecipe(recipe: Recipe)
}