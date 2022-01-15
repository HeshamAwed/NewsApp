package com.hesham.newsapp.ui.favorites_articles

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hesham.newsapp.R

class FavoritesArticlesFragment : Fragment() {

    companion object {
        fun newInstance() = FavoritesArticlesFragment()
    }

    private lateinit var viewModel: FavoritesArticlesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorites_articles_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FavoritesArticlesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}