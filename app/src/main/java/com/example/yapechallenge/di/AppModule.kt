package com.example.yapechallenge.di

import com.example.yapechallenge.core.data.LocalDataSource
import com.example.yapechallenge.core.data.RemoteDataSource
import com.example.yapechallenge.core.repositories.HistoryRepository
import com.example.yapechallenge.core.repositories.RecipeRepository
import com.example.yapechallenge.core.repositories.RecipeRepositoryImpl
import com.example.yapechallenge.data.datasources.LocalDataSourceImpl
import com.example.yapechallenge.data.datasources.RemoteDataSourceImpl
import com.example.yapechallenge.data.ktor.client.KtorClientImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesWeatherApi(): KtorClientImpl {
        return KtorClientImpl()
    }

    @Provides
    fun providesLocalDataSource(): LocalDataSource {
        return LocalDataSourceImpl
    }

    @Provides
    fun providesRemoteDataSource(client: KtorClientImpl): RemoteDataSource {
        return RemoteDataSourceImpl(client)
    }

    @Provides
    fun providesDispatcher(): CoroutineContext {
        return Dispatchers.IO
    }

    @Provides
    fun providesRecipeRepository(remoteDataSource: RemoteDataSource, localDataSource: LocalDataSource, context: CoroutineContext): RecipeRepository {
        return RecipeRepositoryImpl(remoteDataSource, localDataSource, context)
    }

    @Provides
    fun providesHistoryRepository(): HistoryRepository {
        return HistoryRepository
    }

}