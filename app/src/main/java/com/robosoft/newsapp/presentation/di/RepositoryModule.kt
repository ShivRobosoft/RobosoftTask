package com.robosoft.newsapp.presentation.di

import com.robosoft.newsapp.data.repository.NewsRepositoryImpl
import com.robosoft.newsapp.data.repository.datasource.NewsRemoteDataSource
import com.robosoft.newsapp.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideNewsRepository(
        newsRemoteDataSource: NewsRemoteDataSource
    ):NewsRepository{
        return NewsRepositoryImpl(newsRemoteDataSource)
    }
}