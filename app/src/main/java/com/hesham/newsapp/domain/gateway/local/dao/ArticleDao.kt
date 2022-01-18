package com.hesham.newsapp.domain.gateway.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hesham.newsapp.domain.entities.Article

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article")
    fun getFavoritesArticles(): LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

}