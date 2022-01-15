package com.hesham.newsapp.domain.gateway.remote

import com.hesham.newsapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*


private const val AUTHORIZATION = "Authorization"
private const val LANGUAGE = "Language"
private const val BEARER = "Bearer"

class HeaderInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.addHeader(AUTHORIZATION, "$BEARER ${BuildConfig.MY_API_KEY}")
        request.addHeader(LANGUAGE, Locale.getDefault().language)
        return chain.proceed(request.build())
    }
}