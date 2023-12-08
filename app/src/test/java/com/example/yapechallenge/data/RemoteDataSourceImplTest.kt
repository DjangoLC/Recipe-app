package com.example.yapechallenge.data

import com.example.yapechallenge.core.data.RemoteDataSource
import com.example.yapechallenge.core.domain.SearchRecipe
import com.example.yapechallenge.data.datasources.RemoteDataSourceImpl
import com.example.yapechallenge.data.ktor.client.KtorClientImpl
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.mockk.MockKAnnotations.init
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RemoteDataSourceImplTest {

    @MockK
    private lateinit var client: KtorClientImpl

    private lateinit var remoteDataSource: RemoteDataSource

    @Before
    fun setup() {
        init(this)
        remoteDataSource = RemoteDataSourceImpl(client)
    }

    @Test
    fun `given valid query, when findRecipe is called, then return list of SearchRecipe`() = runTest {
        // Arrange
        val query = "apple"
        val expectedRecipes = listOf(SearchRecipe(1, "Apple pie", "url"))
        coEvery { client.findRecipeByName(query) } returns Result.success(expectedRecipes)

        // Act
        val result = remoteDataSource.findRecipe(query)

        // Assert
        result.should {
            it.shouldNotBeEmpty()
            it.first().id shouldBe 1
            it.first().title shouldBe "Apple pie"
        }
    }

    @Test
    fun `given invalid query, when findRecipe is called, then return empty list`() = runTest {
        // Arrange
        val query = "Unknown"
        coEvery { client.findRecipeByName(query) } returns Result.failure(Exception("Not found"))

        // Act
        val result = remoteDataSource.findRecipe(query)

        // Assert
        result.shouldBeEmpty()
    }
}