package com.example.yapechallenge.core.domain

data class SearchRecipe(
    val id: Int,
    val title: String,
    val imageUrl: String
) {
    companion object {
        val DEFAULT = SearchRecipe(-1,"Potates", "https://spoonacular.com/recipeImages/665273-556x370.jpg")
    }
}