package com.hesham.newsapp.ui.articles.search

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.RecyclerView
import com.hesham.newsapp.R
import com.hesham.newsapp.databinding.FragmentSearchBinding
import com.hesham.newsapp.domain.entities.Article
import com.hesham.newsapp.ui.articles.adapters.ArticlesAdapter
import com.hesham.newsapp.ui.articles.favorites.FavoritesArticlesAdapter
import com.hesham.newsapp.ui.articles.home.HomeFragmentDirections
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class SearchFragment : Fragment() {

    private lateinit var searchViewModel: SearchViewModel
    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        searchViewModel =  getViewModel(){ parametersOf(this) }
        binding.bindState(
            uiState = searchViewModel.state,
            pagingData = searchViewModel.pagingDataFlow,
            uiActions = searchViewModel.accept
        )
        return root
    }

    private fun FragmentSearchBinding.bindState(
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<Article>>,
        uiActions: (UiAction) -> Unit
    ) {
        val articlesAdapter = ArticlesAdapter(itemClicked = {
            val action = SearchFragmentDirections.actionNavigationSearchToDetailsFragment(it)
            findNavController().navigate(action)
        }, addToFavorite = {
            searchViewModel.addArticleToFavorite(it)
            Toast.makeText(requireContext(), getString(R.string.article_added_to_favorites), Toast.LENGTH_SHORT).show()
        })
        rvArticles.adapter = articlesAdapter

        bindSearch(
            uiState = uiState,
            onQueryChanged = uiActions
        )
        bindList(
            articlesAdapter = articlesAdapter,
            uiState = uiState,
            pagingData = pagingData,
            onScrollChanged = uiActions
        )
    }


    private fun FragmentSearchBinding.bindSearch(
        uiState: StateFlow<UiState>,
        onQueryChanged: (UiAction.Search) -> Unit
    ) {
        textSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }
        textSearch.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput(onQueryChanged)
                true
            } else {
                false
            }
        }

        lifecycleScope.launch {
            uiState
                .map { it.query }
                .distinctUntilChanged()
                .collect(textSearch::setText)
        }
    }

    private fun FragmentSearchBinding.updateRepoListFromInput(onQueryChanged: (UiAction.Search) -> Unit) {
        textSearch.text.trim().let {
            if (it.isNotEmpty()) {
                rvArticles.scrollToPosition(0)
                onQueryChanged(UiAction.Search(query = it.toString()))
            }
        }
    }

    private fun FragmentSearchBinding.bindList(
        articlesAdapter: ArticlesAdapter,
        uiState: StateFlow<UiState>,
        pagingData: Flow<PagingData<Article>>,
        onScrollChanged: (UiAction.Scroll) -> Unit
    )   {
        rvArticles.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy != 0) onScrollChanged(UiAction.Scroll(currentQuery = uiState.value.query))
            }
        })
        val notLoading = articlesAdapter.loadStateFlow
            // Only emit when REFRESH LoadState for RemoteMediator changes.
            .distinctUntilChangedBy { it.source.refresh }
            // Only react to cases where Remote REFRESH completes i.e., NotLoading.
            .map { it.source.refresh is LoadState.NotLoading }

        val hasNotScrolledForCurrentSearch = uiState
            .map { it.hasNotScrolledForCurrentSearch }
            .distinctUntilChanged()

        val shouldScrollToTop = combine(
            notLoading,
            hasNotScrolledForCurrentSearch,
            Boolean::and
        )
            .distinctUntilChanged()

        lifecycleScope.launch {
            pagingData.collectLatest(articlesAdapter::submitData)
        }

        lifecycleScope.launch {
            shouldScrollToTop.collect { shouldScroll ->
                if (shouldScroll) rvArticles.scrollToPosition(0)
            }
        }
    }
}