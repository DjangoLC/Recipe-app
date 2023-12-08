package com.example.yapechallenge.core.repositories

import javax.inject.Singleton

@Singleton
object HistoryRepository {
    private val cache = mutableListOf<String>()

    fun saveHistory(query: String) {
        cache.add(query)
    }

    fun getHistory(): List<String> = cache

}