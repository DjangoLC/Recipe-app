package com.example.yapechallenge.core.usecases

import com.example.yapechallenge.core.domain.Recipe
import com.example.yapechallenge.core.repositories.RecipeRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllRecipes @Inject constructor(private val recipesRepository: RecipeRepositoryImpl) :
    BaseUseCase<Unit, Flow<List<Recipe>>>() {

    override suspend fun buildUseCase(params: Unit): Flow<List<Recipe>> {
        return recipesRepository.getAllRecipes()
    }

}