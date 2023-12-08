package com.example.yapechallenge.data.datasources

import com.example.yapechallenge.core.data.RemoteDataSource
import com.example.yapechallenge.core.domain.Recipe
import com.example.yapechallenge.core.domain.SearchRecipe
import com.example.yapechallenge.data.ktor.client.KtorClientImpl
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(private val client: KtorClientImpl) : RemoteDataSource {

    override suspend fun findRecipe(query: String): List<SearchRecipe> {
        return client.findRecipeByName(query).getOrNull() ?: emptyList()
    }

    override suspend fun getRecipe(id: Int): Recipe {
        return client.getRecipe(id).getOrNull() ?: Recipe.DEFAULT
    }

    override suspend fun getAllRecipes(): List<Recipe> {
        return client.getRandomRecipes().getOrNull() ?: emptyList()
    }

}