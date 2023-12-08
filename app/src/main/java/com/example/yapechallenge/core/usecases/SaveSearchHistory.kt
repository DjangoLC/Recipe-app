package com.example.yapechallenge.core.usecases

import com.example.yapechallenge.core.repositories.HistoryRepository
import javax.inject.Inject

class SaveSearchHistory @Inject constructor(
    private val historyRepository: HistoryRepository
) : BaseUseCase<SaveSearchHistory.Params, Unit>() {

    override suspend fun buildUseCase(params: Params) {
        return historyRepository.saveHistory(params.query)
    }

    class Params(val query: String)
}