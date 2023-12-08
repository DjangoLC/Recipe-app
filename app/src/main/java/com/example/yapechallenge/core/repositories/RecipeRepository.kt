package com.example.yapechallenge.core.repositories

import com.example.yapechallenge.core.domain.Recipe
import com.example.yapechallenge.core.domain.SearchRecipe
import kotlinx.coroutines.flow.Flow


interface RecipeRepository {
    suspend fun findRecipe(query: String): List<SearchRecipe>
    suspend fun getRecipe(id: Int): Recipe
    suspend fun getAllRecipes(): Flow<List<Recipe>>
    suspend fun saveRecipes(recipes: List<Recipe>)
}