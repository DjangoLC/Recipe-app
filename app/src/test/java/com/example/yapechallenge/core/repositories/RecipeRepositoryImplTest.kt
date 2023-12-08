package com.example.yapechallenge.core.repositories

import app.cash.turbine.test
import com.example.yapechallenge.core.data.LocalDataSource
import com.example.yapechallenge.core.data.RemoteDataSource
import com.example.yapechallenge.core.domain.Recipe
import com.example.yapechallenge.core.domain.SearchRecipe
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.MockKAnnotations.init
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RecipeRepositoryImplTest {

    @MockK
    private lateinit var localDataSource: LocalDataSource

    @MockK
    private lateinit var remoteDataSource: RemoteDataSource

    private lateinit var repository: RecipeRepository

    @Before
    fun setup() {
        init(this)
        //needed for the prepopulate db in RecipeRepositoryImpl
        val expectedRecipes = (1..10).map { Recipe.DEFAULT }
        coEvery { remoteDataSource.getAllRecipes() } returns expectedRecipes
        coEvery { localDataSource.saveRecipes(any()) } just Runs

        repository = RecipeRepositoryImpl(
            remoteDataSource, localDataSource, Dispatchers.Unconfined
        )
    }

    @Test
    fun `given a call of findRecipe, when findRecipe is called, then return a valid searchRecipe`() =
        runTest {
            // Arrange
            val query = "cacao"
            val expectedSearchRecipe = (1..10).map { SearchRecipe.DEFAULT.copy(title = query) }

            coEvery { remoteDataSource.findRecipe(query) } returns expectedSearchRecipe
            coEvery { localDataSource.getAllSearchRecipes() } returns flowOf(expectedSearchRecipe)
            coEvery { localDataSource.saveSearchRecipe(any()) } just runs

            // Act
            val result = repository.findRecipe(query)

            // Assert
            coVerify(exactly = 1) { remoteDataSource.findRecipe(any()) }
            coVerify(exactly = 1) { localDataSource.saveSearchRecipe(any()) }
            coVerify(exactly = 1) { localDataSource.saveRecipes(any()) }
            coVerify(exactly = 1) { localDataSource.getAllSearchRecipes() }
            result.should {
                it.shouldNotBeEmpty()
                it.size.shouldBe(expectedSearchRecipe.size)
                it.filter { it.title.contains(query) }.size.shouldNotBe(0)
            }
        }

    @Test
    fun `given a valid id, when getRecipe is called, then return Recipe`() = runTest {
        // Arrange
        val recipeId = -1
        val recipeName = "Cacao Maca Walnut Milk"
        val expectedRecipe = Recipe.DEFAULT

        coEvery { remoteDataSource.getRecipe(recipeId) } returns expectedRecipe

        // Act
        val result = repository.getRecipe(recipeId)

        // Assert
        result.id shouldBe recipeId
        result.name shouldBe recipeName
        coVerify(exactly = 1) { remoteDataSource.getRecipe(recipeId) }
    }

    @Test
    fun `when a call on getAllRecipes, then return Flow of List of Recipe`() = runTest {
        // Arrange
        val expectedRecipes = (1..10).map { Recipe.DEFAULT }
        val flowOfRecipes = flowOf(expectedRecipes)

        coEvery { localDataSource.getAllRecipes() } returns flowOfRecipes

        // Act
        val result = repository.getAllRecipes()

        // Assert
        result.test {
            val emittedRecipes = awaitItem()
            emittedRecipes shouldBe expectedRecipes
            cancelAndIgnoreRemainingEvents()
        }
        coVerify(exactly = 1) { localDataSource.getAllRecipes() }
    }

    @Test
    fun `given a call on list of recipes, when saveRecipes is called, then verify recipes not empty`() =
        runTest {
            // Arrange
            val recipesExpected = (1..10).map { Recipe.DEFAULT }

            coEvery { remoteDataSource.getAllRecipes() } returns recipesExpected
            coEvery { localDataSource.getAllRecipes() } returns flowOf(recipesExpected)

            // Act
            val result = mutableListOf<Recipe>()
            repository.getAllRecipes().test {
                result.addAll(this.awaitItem())
                cancelAndIgnoreRemainingEvents()
            }

            // Assert
            coVerify(exactly = 1) { localDataSource.getAllRecipes() }
            result.should {
                it.size.shouldBe(recipesExpected.size)
            }
        }

}