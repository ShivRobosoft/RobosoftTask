package com.robosoft.newsapp.presentation.di

import com.robosoft.newsapp.domain.repository.NewsRepository
import com.robosoft.newsapp.domain.usecase.GetNewsHeadlinesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {


    @Singleton
    @Provides
    fun provideGetNewsheadLinesUseCase(
      newsRepository: NewsRepository
    ):GetNewsHeadlinesUseCase{
     return GetNewsHeadlinesUseCase(newsRepository)
    }
}