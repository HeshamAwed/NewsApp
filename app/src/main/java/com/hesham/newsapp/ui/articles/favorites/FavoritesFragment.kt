package com.hesham.newsapp.ui.articles.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hesham.newsapp.databinding.FragmentFavoritesBinding
import com.hesham.newsapp.ui.kts.showError
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val favoritesViewModel: FavoritesViewModel by viewModel()
    private var _binding: FragmentFavoritesBinding? = null


    val articlesAdapter = FavoritesArticlesAdapter(itemClicked = {
        val action = FavoritesFragmentDirections.actionNavigationFavoritesToDetailsFragment(it)
        findNavController().navigate(action)
    }, removeFromFavorite = {
        favoritesViewModel.removeArticleFromFavorite(it)
    })

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvArticles.adapter = articlesAdapter
        initObservers()
    }

    private fun initObservers() {
        favoritesViewModel.getAllFavorites().observe(viewLifecycleOwner,{
            binding.textEmpty.isVisible = it.isEmpty()
            Log.e("Hesham",it.toString()+" ")
            articlesAdapter.submitList(it)
            articlesAdapter.notifyDataSetChanged()
        })
        favoritesViewModel.errorMessage.observe(viewLifecycleOwner,{
            showError(it)
        })
       // favoritesViewModel.getAllFavorites()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}