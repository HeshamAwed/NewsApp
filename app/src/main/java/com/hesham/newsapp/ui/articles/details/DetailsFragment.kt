package com.hesham.newsapp.ui.articles.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.hesham.newsapp.R
import com.hesham.newsapp.databinding.DetailsFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment() {


    private  val  detailsViewModel: DetailsViewModel by viewModel()
    private val args: DetailsFragmentArgs by navArgs()

    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailsFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = detailsViewModel
        detailsViewModel.article.value = args.article
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}