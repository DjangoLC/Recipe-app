package com.example.yapechallenge.core.usecases

abstract class BaseUseCase<P, R>() {
    abstract suspend fun buildUseCase(params: P): R
}