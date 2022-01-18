package com.hesham.newsapp.ui.articles.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.hesham.newsapp.R
import com.hesham.newsapp.databinding.FragmentHomeBinding
import com.hesham.newsapp.ui.articles.adapters.ArticleLoadStateAdapter
import com.hesham.newsapp.ui.articles.adapters.ArticlesAdapter
import com.hesham.newsapp.ui.kts.showError
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    val articlesAdapter = ArticlesAdapter(itemClicked = {
        val action = HomeFragmentDirections.actionNavigationHomeToDetailsFragment(it)
        findNavController().navigate(action)
    }, addToFavorite = {
        homeViewModel.addArticleToFavorite(it)
        Toast.makeText(requireContext(), getString(R.string.article_added_to_favorites), Toast.LENGTH_SHORT).show()
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapters()
        loadData()
        initObservers()
    }

    private fun initObservers() {
        homeViewModel.errorMessage.observe(viewLifecycleOwner,{
           showError(it)
        })

        homeViewModel.isLoading.observe(viewLifecycleOwner,{
            binding.articlesProgressBar.isVisible = it
        })
        homeViewModel.isEmptyList.observe(viewLifecycleOwner,{
            binding.textEmpty.isVisible = it
        })
    }


    private fun initAdapters() {
        val loadStateAdapter = ArticleLoadStateAdapter{articlesAdapter.retry()}
        binding.rvArticles.adapter = articlesAdapter.withLoadStateFooter(loadStateAdapter)
        articlesAdapter.addLoadStateListener {
            homeViewModel.handleLoadStates(it, articlesAdapter.itemCount)
        }
    }

    private fun loadData() {
        lifecycleScope.launch {
            homeViewModel.getBreakingArticles().collectLatest {
                articlesAdapter.submitData(it)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}