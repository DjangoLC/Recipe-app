package com.example.yapechallenge.core.data

import com.example.yapechallenge.core.domain.Recipe
import com.example.yapechallenge.core.domain.SearchRecipe
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun findRecipe(query: String): List<SearchRecipe>
    suspend fun getRecipe(id: Int): Recipe
    suspend fun getAllRecipes(): List<Recipe>
}