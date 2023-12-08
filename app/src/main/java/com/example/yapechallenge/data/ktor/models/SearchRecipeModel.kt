package com.example.yapechallenge.data.ktor.models

import kotlinx.serialization.Serializable

@Serializable
class SearchRecipeResult(val results: List<SearchRecipeModel>)

@Serializable
class SearchRecipeModel(
    val id: Int,
    val title: String,
    val image: String
)