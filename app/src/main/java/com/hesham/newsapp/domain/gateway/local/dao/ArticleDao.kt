package com.hesham.newsapp.domain.gateway.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Delete
import com.hesham.newsapp.domain.entities.Article
import java.util.concurrent.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article")
     fun getFavoritesArticles(): LiveData<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(article: Article)

    @Delete
    suspend fun deleteArticle(article: Article)

}