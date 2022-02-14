package com.robosoft.newsapp.presentation.di

import com.robosoft.newsapp.data.api.NewsAPIService
import com.robosoft.newsapp.data.repository.datasource.NewsRemoteDataSource
import com.robosoft.newsapp.data.repository.datasourceimpl.NewsRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RemoteDataModule {
    @Singleton
    @Provides
    fun provideNewsRemoteDataSource(
     newsAPIService: NewsAPIService
    ):NewsRemoteDataSource{
     return NewsRemoteDataSourceImpl(newsAPIService)
    }
}