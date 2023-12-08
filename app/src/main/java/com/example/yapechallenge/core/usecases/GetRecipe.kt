package com.example.yapechallenge.core.usecases

import com.example.yapechallenge.core.domain.Recipe
import com.example.yapechallenge.core.repositories.RecipeRepositoryImpl
import javax.inject.Inject

class GetRecipe @Inject constructor(private val recipesRepository: RecipeRepositoryImpl) :
    BaseUseCase<GetRecipe.Params, Recipe>() {

    class Params(val id: Int)

    override suspend fun buildUseCase(params: Params): Recipe {
        return recipesRepository.getRecipe(params.id)
    }

}