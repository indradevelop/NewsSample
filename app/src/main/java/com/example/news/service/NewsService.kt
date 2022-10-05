package com.example.news.service

import com.example.news.api.AppConstants
import com.example.news.api.RetrofitFactory
import com.example.news.model.NewsResult
import com.example.news.model.SourceResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface NewsService {

    @GET("everything")
    suspend fun getEverything(@QueryMap params:HashMap<String, String>): Response<NewsResult?>

    @GET("top-headlines")
    suspend fun getTopHeadlines(@QueryMap params:HashMap<String, String>): Response<NewsResult?>

    @GET("top-headlines/sources")
    suspend fun getSource(@QueryMap params:HashMap<String, String>): Response<SourceResult?>


    companion object {
        /*fun create(sessionManager: SessionManager) : TransactionService {
            return RetrofitFactory(sessionManager).retrofit(AppConstants.urlGateway).create(
                TransactionService::class.java)
        }*/
        fun create() : NewsService {
            return RetrofitFactory().retrofit(AppConstants.urlGateway).create(
                NewsService::class.java)
        }
    }
}
