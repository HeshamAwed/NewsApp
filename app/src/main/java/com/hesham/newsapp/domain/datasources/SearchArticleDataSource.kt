package com.hesham.newsapp.domain.datasources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hesham.newsapp.domain.entities.Article
import com.hesham.newsapp.domain.gateway.remote.AppGateway
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException


class SearchArticleDataSource(val appGateway: AppGateway, val query: String) :
    PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val page = params.key ?: 1
            val response = appGateway.searchNews(query, page)
            LoadResult.Page(
                data = response.articles ?: listOf(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        } catch (exception: UnknownHostException) {
            return LoadResult.Error(exception)
        }
    }
}