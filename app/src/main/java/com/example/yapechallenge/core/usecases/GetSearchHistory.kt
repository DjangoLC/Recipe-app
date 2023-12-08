package com.example.yapechallenge.core.usecases

import com.example.yapechallenge.core.repositories.HistoryRepository
import javax.inject.Inject

class GetSearchHistory@Inject constructor (private val repository: HistoryRepository) :
    BaseUseCase<Unit, List<String>>() {

    override suspend fun buildUseCase(params: Unit): List<String> {
        return repository.getHistory()
    }

}