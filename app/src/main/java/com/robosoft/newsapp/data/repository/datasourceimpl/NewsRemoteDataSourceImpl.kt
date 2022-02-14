package com.robosoft.newsapp.data.repository.datasourceimpl

import com.robosoft.newsapp.data.api.NewsAPIService
import com.robosoft.newsapp.data.model.APIResponse
import com.robosoft.newsapp.data.repository.datasource.NewsRemoteDataSource
import retrofit2.Response

class NewsRemoteDataSourceImpl(
    private val newsAPIService:NewsAPIService,

):NewsRemoteDataSource {

    override suspend fun getTopHeadlines(country:String,page:Int,pageSize:Int): Response<APIResponse> {
         return newsAPIService.getTopHeadlines(country,page,pageSize)
    }
}