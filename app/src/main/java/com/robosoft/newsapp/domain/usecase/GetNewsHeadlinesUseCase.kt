package com.robosoft.newsapp.domain.usecase

import com.robosoft.newsapp.data.model.APIResponse
import com.robosoft.newsapp.data.util.Resource
import com.robosoft.newsapp.domain.repository.NewsRepository

class GetNewsHeadlinesUseCase(private val newsRepository: NewsRepository) {

  suspend fun execute(country:String,page:Int,pageSize:Int):Resource<APIResponse>{
      return newsRepository.getNewsHeadLines(country,page,pageSize)
  }

}