package com.hesham.newsapp.domain.entities

data class ArticlesResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)
