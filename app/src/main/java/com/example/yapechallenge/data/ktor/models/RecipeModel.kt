package com.example.yapechallenge.data.ktor.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Recipes(val recipes: List<RecipeModel>)

@Serializable
class RecipeModel(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val name: String?,
    @SerialName("readyInMinutes")
    val timePreparation: Int?,
    @SerialName("image")
    val imageUrl: String? = null,
    @SerialName("summary")
    val summary: String?,
    @SerialName("instructions")
    val instructions: String?,
    @SerialName("extendedIngredients")
    val ingredients: List<ExtendedIngredients>?
)

@Serializable
data class ExtendedIngredients(
    @SerialName("name")
    val name: String?
)



