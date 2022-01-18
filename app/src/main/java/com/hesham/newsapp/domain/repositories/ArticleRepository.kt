package com.hesham.newsapp.domain.repositories

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.hesham.newsapp.domain.datasources.ArticleDataSource
import com.hesham.newsapp.domain.datasources.SearchArticleDataSource
import com.hesham.newsapp.domain.entities.Article
import com.hesham.newsapp.domain.gateway.local.dao.ArticleDao
import com.hesham.newsapp.domain.gateway.remote.AppGateway
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    fun getBreakingNews(): Flow<PagingData<Article>>
    fun searchNews(query:String): Flow<PagingData<Article>>
    fun getAllFavoriteArticles():LiveData<List<Article>>
    fun addArticleToFavorite(article: Article)
  suspend  fun deleteArticleFromFavorite(article: Article)
}

class ArticleRepositoryImpl(
    private val appGateway: AppGateway,
    private val articleDao: ArticleDao
) : ArticleRepository {
    override fun getBreakingNews(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ArticleDataSource(appGateway) }
        ).flow
    }

    override fun searchNews(query: String): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchArticleDataSource(appGateway,query) }
        ).flow
    }

    override  fun getAllFavoriteArticles(): LiveData<List<Article>> {
       return articleDao.getFavoritesArticles()
    }

    override fun addArticleToFavorite(article: Article) {
        articleDao.insert(article)
    }

    override suspend fun deleteArticleFromFavorite(article: Article) {
        articleDao.deleteArticle(article)
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 50
    }
}
