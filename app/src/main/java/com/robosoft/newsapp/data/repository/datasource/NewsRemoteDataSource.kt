package com.robosoft.newsapp.data.repository.datasource

import com.robosoft.newsapp.data.model.APIResponse
import retrofit2.Response

interface NewsRemoteDataSource {
    suspend fun getTopHeadlines(country:String,page:Int,pageSize:Int):Response<APIResponse>
}