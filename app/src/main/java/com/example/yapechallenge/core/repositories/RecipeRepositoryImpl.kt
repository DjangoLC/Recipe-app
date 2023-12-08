package com.example.yapechallenge.core.repositories

import com.example.yapechallenge.core.data.LocalDataSource
import com.example.yapechallenge.core.data.RemoteDataSource
import com.example.yapechallenge.core.domain.Recipe
import com.example.yapechallenge.core.domain.SearchRecipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RecipeRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val context: CoroutineContext
) : RecipeRepository {

    init {
        // pre populate database
        CoroutineScope(context).launch {
            remoteDataSource.getAllRecipes().also {
                localDataSource.saveRecipes(it)
            }
        }
    }

    override suspend fun findRecipe(query: String): List<SearchRecipe> {
        return withContext(context) {
            remoteDataSource.findRecipe(query).also {
                localDataSource.saveSearchRecipe(it)
                localDataSource.getAllSearchRecipes()
            }
        }
    }

    override suspend fun getRecipe(id: Int): Recipe {
        return withContext(context) {
            remoteDataSource.getRecipe(id)
        }
    }

    override suspend fun getAllRecipes(): Flow<List<Recipe>> {
        return withContext(context) {
            localDataSource.getAllRecipes()
        }
    }

    override suspend fun saveRecipes(recipes: List<Recipe>) {
        return withContext(context) {
            localDataSource.saveRecipes(recipes)
        }
    }
}