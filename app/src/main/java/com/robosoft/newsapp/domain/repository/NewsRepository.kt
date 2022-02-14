package com.robosoft.newsapp.domain.repository

import com.robosoft.newsapp.data.model.APIResponse
import com.robosoft.newsapp.data.util.Resource

interface NewsRepository{

    suspend fun getNewsHeadLines(country:String,page:Int,pageSize:Int):Resource<APIResponse>


}