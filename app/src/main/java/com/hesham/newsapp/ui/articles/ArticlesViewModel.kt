package com.hesham.newsapp.ui.articles

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ArticlesViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)

    fun isEmptyList():Boolean{
        return false
    }
}