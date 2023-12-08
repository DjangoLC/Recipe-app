package com.example.yapechallenge.data

import com.example.yapechallenge.core.domain.Recipe
import com.example.yapechallenge.core.domain.SearchRecipe
import com.example.yapechallenge.data.ktor.models.RecipeModel
import com.example.yapechallenge.data.ktor.models.SearchRecipeModel

fun RecipeModel.toRecipe() =
    Recipe(
        id = this.id,
        name = this.name.orEmpty(),
        description = this.summary.orEmpty(),
        imageUrl = this.imageUrl.orEmpty(),
        ingredients = this.ingredients?.map { it.name.orEmpty() }.orEmpty(),
        instructions = this.instructions.orEmpty(),
        timePreparation = timePreparation?: 0
    )

fun SearchRecipeModel.toSearchRecipe() =
    SearchRecipe(
        id = id,
        title = title,
        imageUrl = image
    )