package com.hesham.newsapp.ui.articles.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hesham.newsapp.application.launchDataLoad
import com.hesham.newsapp.domain.entities.Article
import com.hesham.newsapp.domain.repositories.ArticleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val articleRepository: ArticleRepository) : ViewModel() {
    val isEmptyList = MutableLiveData<Boolean>()
    val isLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<Throwable>()

    fun getBreakingArticles() :Flow<PagingData<Article>>{
      return  articleRepository.getBreakingNews().cachedIn(viewModelScope)
    }

    fun handleLoadStates(combinedLoadStates: CombinedLoadStates, itemCount: Int) {
        when {
            combinedLoadStates.refresh is LoadState.Error || combinedLoadStates.append is LoadState.Error || combinedLoadStates.prepend is LoadState.Error -> {
                isLoading.postValue(false)
                isEmptyList.postValue(itemCount == 0)
                val error = when {
                    combinedLoadStates.refresh is LoadState.Error -> combinedLoadStates.refresh as LoadState.Error
                    combinedLoadStates.append is LoadState.Error -> combinedLoadStates.append as LoadState.Error
                    combinedLoadStates.prepend is LoadState.Error -> combinedLoadStates.prepend as LoadState.Error
                    else -> null
                }
                if (error != null) {
                    viewModelScope.launch {
                        errorMessage.postValue(error.error)
                    }
                }
            }
            combinedLoadStates.refresh is LoadState.NotLoading && combinedLoadStates.append is LoadState.NotLoading && combinedLoadStates.prepend is LoadState.NotLoading -> {
                isLoading.postValue(false)
                isEmptyList.postValue(itemCount == 0)
            }

            combinedLoadStates.refresh is LoadState.Loading && combinedLoadStates.append is LoadState.Loading && combinedLoadStates.prepend is LoadState.Loading -> {
                isEmptyList.postValue(itemCount == 0)
                if (itemCount == 0) {
                    isLoading.postValue(true)
                }
            }
        }
    }

    fun  addArticleToFavorite(article: Article)= articleRepository.addArticleToFavorite(article)

}