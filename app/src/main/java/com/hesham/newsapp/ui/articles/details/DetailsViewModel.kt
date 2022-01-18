package com.hesham.newsapp.ui.articles.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hesham.newsapp.domain.entities.Article

class DetailsViewModel : ViewModel() {
   val article = MutableLiveData<Article>()
}