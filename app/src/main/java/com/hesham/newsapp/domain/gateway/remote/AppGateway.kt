package com.hesham.newsapp.domain.gateway.remote

import com.hesham.newsapp.domain.entities.ArticlesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AppGateway {
    companion object {
        const val apiPath = ""
    }


    @GET("everything?sortBy=publishedAt")
    fun getArticles(
        @Query("q") keyword: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
       // @Header("X-Api-Key") apiKey: String
    ): Call<ArticlesResponse>
}