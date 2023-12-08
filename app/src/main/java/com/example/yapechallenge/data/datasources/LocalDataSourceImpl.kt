package com.example.yapechallenge.data.datasources

import com.example.yapechallenge.core.data.LocalDataSource
import com.example.yapechallenge.core.domain.Recipe
import com.example.yapechallenge.core.domain.SearchRecipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

object LocalDataSourceImpl : LocalDataSource {

    private val db = mutableListOf<Recipe>()
    private val dbSearch = mutableListOf<SearchRecipe>()

    private val _cache = MutableSharedFlow<List<Recipe>>(replay = 1)
    private val _dbSearch = MutableSharedFlow<List<SearchRecipe>>(replay = 1)

    private val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun getRecipe(id: Int): Recipe {
        return db.firstOrNull { it.id == id } ?: Recipe.EMPTY
    }

    override suspend fun getAllRecipes(): Flow<List<Recipe>> {
        return _cache.asSharedFlow()
    }

    override suspend fun getAllSearchRecipes(): Flow<List<SearchRecipe>> {
        return _dbSearch.asSharedFlow()
    }

    override suspend fun saveRecipes(recipes: List<Recipe>) {
        updateFlow(recipes)
    }

    override suspend fun saveSearchRecipe(recipes: List<SearchRecipe>) {
        updateSearchFlow(recipes)
    }

    override suspend fun saveRecipe(recipe: Recipe) {
        return updateFlow(recipe)
    }

    private fun updateFlow(recipe: Recipe) {
        db.add(recipe)
        scope.launch {
            _cache.emit(db)
        }
    }

    private fun updateFlow(recipes: List<Recipe>) {
        db.addAll(recipes)
        scope.launch {
            _cache.emit(db)
        }
    }

    private fun updateSearchFlow(recipes: List<SearchRecipe>) {
        dbSearch.addAll(recipes)
        scope.launch {
            _dbSearch.emit(dbSearch)
        }
    }
}