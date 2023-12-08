package com.example.yapechallenge.core.usecases

import com.example.yapechallenge.core.domain.SearchRecipe
import com.example.yapechallenge.core.repositories.RecipeRepository
import javax.inject.Inject

class FindRecipe @Inject constructor(private val repository: RecipeRepository) :
    BaseUseCase<FindRecipe.Params, List<SearchRecipe>>() {


    class Params(val query: String)

    override suspend fun buildUseCase(params: Params): List<SearchRecipe> {
        return repository.findRecipe(params.query)
    }

}