package com.robosoft.newsapp.data.repository

import com.robosoft.newsapp.data.model.APIResponse
import com.robosoft.newsapp.data.repository.datasource.NewsRemoteDataSource
import com.robosoft.newsapp.data.util.Resource
import com.robosoft.newsapp.domain.repository.NewsRepository
import retrofit2.Response

class NewsRepositoryImpl(
    private val newsRemoteDataSource: NewsRemoteDataSource
):NewsRepository {
    override suspend fun getNewsHeadLines(country:String,page:Int,pageSize:Int): Resource<APIResponse> {
        return responseToResource(newsRemoteDataSource.getTopHeadlines(country,page,pageSize))
    }

    private fun responseToResource(response: Response<APIResponse>):Resource<APIResponse>{
        if (response.isSuccessful){
            response.body()?.let {result->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
}