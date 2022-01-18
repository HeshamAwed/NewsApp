package com.hesham.newsapp.ui.articles.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hesham.newsapp.application.launchDataLoad
import com.hesham.newsapp.domain.entities.Article
import com.hesham.newsapp.domain.repositories.ArticleRepository

class FavoritesViewModel(private val articleRepository: ArticleRepository) : ViewModel() {
    private val _favoritesArticles = MutableLiveData<List<Article>>()
    val favoritesArticles: LiveData<List<Article>> = _favoritesArticles
    val errorMessage = MutableLiveData<Throwable>()

    fun getAllFavorites(): LiveData<List<Article>> = articleRepository.getAllFavoriteArticles()

    fun removeArticleFromFavorite(article: Article) {
        launchDataLoad(
            execution = {
                articleRepository.deleteArticleFromFavorite(article)
            },
            errorReturned = { throwable ->
                errorMessage.postValue(throwable)
            }, finallyBlock = {
                getAllFavorites()
            })
    }
}