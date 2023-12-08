package com.example.yapechallenge.core.domain

data class Recipe(
    val id: Int = -1,
    val name: String,
    val description: String,
    val instructions: String,
    val imageUrl: String,
    val timePreparation: Int,
    val ingredients: List<String> = emptyList()
) {
    companion object {
        val DEFAULT = Recipe(
            name = "Cacao Maca Walnut Milk",
            imageUrl = "https://www.themealdb.com/images/media/meals/ustsqw14682500a14.jpg",
            description = "Your recipe has been uploaded, you can see it on your profile. Your recipe has been uploaded, you can see it on your",
            instructions = "Your recipe has been uploaded, you can see it on your profile. Your recipe has been uploaded, you can see it on your",
            timePreparation = 0,
            ingredients = listOf(
                "4 Eggs",
                "1/2 Butter",
                "1/2 Butter"
            )
        )
        val EMPTY = Recipe(
            name = "",
            imageUrl = "",
            description = "",
            instructions = "",
            timePreparation = 0,
            ingredients = listOf(
                "",
                "",
                ""
            )
        )
    }
}