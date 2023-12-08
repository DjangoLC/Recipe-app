package com.example.yapechallenge.data

import app.cash.turbine.test
import com.example.yapechallenge.core.domain.Recipe
import com.example.yapechallenge.core.domain.SearchRecipe
import com.example.yapechallenge.data.datasources.LocalDataSourceImpl
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LocalDataSourceImplTest {

    private val localDataSource = LocalDataSourceImpl

    @Test
    fun `given existing id, when getRecipe is called, then return Recipe`() = runTest {
        // Arrange
        val recipe = Recipe.DEFAULT.copy(id = 5)
        val expectedId = 5
        localDataSource.saveRecipe(recipe)

        // Act
        val result = localDataSource.getRecipe(5)

        // Assert
        result.id.shouldBe(expectedId)
    }

    @Test
    fun `when getAllRecipes is called, then return Flow of List of Recipe`() = runTest {
        // Arrange
        val recipe1 = Recipe.DEFAULT.copy(id = 4)
        val recipe2 = Recipe.DEFAULT.copy(id = 3)
        localDataSource.saveRecipes(listOf(recipe1, recipe2))

        // Act
        val results = mutableListOf<Recipe>()
        localDataSource.getAllRecipes().test {
            val recipes = awaitItem()
            results.addAll(recipes)
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        results.should { recipes ->
            recipes.size shouldBe 2
            recipes[0].id shouldBe 4
            recipes[1].id shouldBe 3
        }
    }

    @Test
    fun `when getAllSearchRecipes is called, then return Flow of List of SearchRecipe`() = runTest {
        // Arrange
        val searchRecipe1 = SearchRecipe.DEFAULT.copy(title = "Pork")
        val searchRecipe2 = SearchRecipe.DEFAULT.copy(title = "Cornish")
        localDataSource.saveSearchRecipe(listOf(searchRecipe1, searchRecipe2))

        // Act
        val results = mutableListOf<SearchRecipe>()
        localDataSource.getAllSearchRecipes().test {
            val searchRecipes = awaitItem()
            results.addAll(searchRecipes)
            cancelAndIgnoreRemainingEvents()
        }

        // Assert
        results.should { searchRecipes ->
            searchRecipes.size shouldBe 2
            searchRecipes[0].title shouldBe "Pork"
            searchRecipes[1].title shouldBe "Cornish"
        }
    }

}