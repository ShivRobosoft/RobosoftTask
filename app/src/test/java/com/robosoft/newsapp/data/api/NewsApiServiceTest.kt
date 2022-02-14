package com.robosoft.newsapp.data.api

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.nio.charset.Charset

class NewsApiServiceTest {

    private lateinit var service:NewsAPIService
    private lateinit var server:MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(server.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsAPIService::class.java)
    }


    private fun enqueueMockResponse(
        filename:String){
        val inputStream = javaClass.classLoader!!.getResourceAsStream(filename)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()
        mockResponse.setBody(source.readString(Charsets.UTF_8))
        server.enqueue(mockResponse)
    }


    @Test
    fun getTopHeadLines_sentRequest_receiveExpected(){

        runBlocking {
            enqueueMockResponse("newsresponse.json")
          val responsebody=  service.getTopHeadlines("us",1).body()
            val request = server.takeRequest()
            assertThat(responsebody).isNotNull()
            assertThat(request.path).isEqualTo("/v2/top-headlines?country=us&page=1&apiKey=21025abd9eb5410ea089d790802971f0")
        }
    }

     @Test
    fun getTopHeadLines_receivedResponse_correctPageSize(){
        runBlocking {
            enqueueMockResponse("newsresponse.json")
            val responsebody=  service.getTopHeadlines("us",1).body()
            val articleList = responsebody!!.articles
            assertThat(articleList.size).isEqualTo(20)
        }
    }



    @Test
    fun getTopHeadLines_receivedResponse_correctContent(){
        runBlocking {
            enqueueMockResponse("newsresponse.json")
            val responsebody=  service.getTopHeadlines("us",1).body()
            val articleList = responsebody!!.articles
            val article= articleList[0]
            assertThat(article.author).isEqualTo("Ryan Craddock")
            assertThat(article.url).isEqualTo("https://www.nintendolife.com/guides/where-to-pre-order-mario-strikers-battle-league-on-switch")
        }
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}