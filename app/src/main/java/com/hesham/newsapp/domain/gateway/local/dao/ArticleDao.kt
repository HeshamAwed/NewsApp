package com.hesham.newsapp.domain.gateway.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Delete
import com.hesham.newsapp.domain.entities.Article

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article WHERE filter_type =:queryString ORDER BY voteAverage desc")
    fun getArticleList(queryString: String): PagingSource<Int, Article>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(list: List<Article>)

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("DELETE FROM article WHERE id = :id")
    suspend fun deleteArticle(id: String)

    @Query("DELETE FROM article")
    suspend fun deleteAll()
}