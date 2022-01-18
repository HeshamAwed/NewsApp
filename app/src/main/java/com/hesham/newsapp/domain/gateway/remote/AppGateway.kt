package com.hesham.newsapp.domain.gateway.remote

import com.hesham.newsapp.domain.entities.ArticlesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AppGateway {
    companion object {
        const val apiPath = ""
    }

    @GET("top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode: String = "eg",
        @Query("page")
        pageNumber: Int
    ): ArticlesResponse


    @GET("everything")
    suspend fun searchNews(
        @Query("q")
        searchQuery: String,
        @Query("page")
        pageNumber: Int
    ): ArticlesResponse
}