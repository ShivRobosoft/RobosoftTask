package com.robosoft.newsapp.presentation.di


import com.robosoft.newsapp.BuildConfig
import com.robosoft.newsapp.data.api.NewsAPIService
import com.robosoft.newsapp.data.repository.NewsRepositoryImpl
import com.robosoft.newsapp.data.repository.datasource.NewsRemoteDataSource
import com.robosoft.newsapp.data.repository.datasourceimpl.NewsRemoteDataSourceImpl
import com.robosoft.newsapp.domain.repository.NewsRepository
import com.robosoft.newsapp.domain.usecase.GetNewsHeadlinesUseCase
import com.robosoft.newsapp.presentation.viewmodel.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val mainModule = module {

    factory<NewsRepository> {
        NewsRepositoryImpl(get())
    }

    factory  {
        GetNewsHeadlinesUseCase(get())

    }

    factory <NewsRemoteDataSource>{
        NewsRemoteDataSourceImpl(get())
    }


    viewModel {
           NewsViewModel(get(),get())
    }

    factory {
        provideNewsAPIService(get())
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}

fun provideNewsAPIService(retrofit: Retrofit): NewsAPIService {
    return retrofit.create(NewsAPIService::class.java)
}
