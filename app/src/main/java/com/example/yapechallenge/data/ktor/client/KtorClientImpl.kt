package com.example.yapechallenge.data.ktor.client

import android.util.Log
import com.example.yapechallenge.core.domain.Recipe
import com.example.yapechallenge.core.domain.SearchRecipe
import com.example.yapechallenge.data.ktor.models.RecipeModel
import com.example.yapechallenge.data.ktor.models.Recipes
import com.example.yapechallenge.data.ktor.models.SearchRecipeResult
import com.example.yapechallenge.data.toRecipe
import com.example.yapechallenge.data.toSearchRecipe
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.DefaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.observer.ResponseObserver
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json

class KtorClientImpl {

    private val TIME_OUT = 6000

    private val client = HttpClient(Android) {

        install(JsonFeature) {

            serializer = KotlinxSerializer(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })

            engine {
                connectTimeout = TIME_OUT
                socketTimeout = TIME_OUT
            }
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v("Logger Ktor =>", message)
                }

            }
            level = LogLevel.ALL
        }

        install(ResponseObserver) {
            onResponse { response ->
                Log.d("HTTP status:", "${response.status.value}")
            }
        }

        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header("x-api-key", "04119aab77a948c5b202af2634f09955")
        }
    }

    private val baseUrl = "https://api.spoonacular.com/"

    suspend fun findRecipeByName(query: String): Result<List<SearchRecipe>> {
        return kotlin.runCatching {
            client.get<SearchRecipeResult>("$baseUrl/recipes/complexSearch") {
                parameter("query", query)
            }.results.map { it.toSearchRecipe() }
        }
    }

    suspend fun getRandomRecipes(): Result<List<Recipe>> {
        return kotlin.runCatching {
            client.get<Recipes>("$baseUrl/recipes/random") {
                parameter("number", 10)
            }.recipes.map { it.toRecipe() }
        }
    }

    suspend fun getRecipe(id: Int): Result<Recipe> {
        return kotlin.runCatching {
            client.get<RecipeModel>("$baseUrl/recipes/$id/information").toRecipe()
        }
    }

}