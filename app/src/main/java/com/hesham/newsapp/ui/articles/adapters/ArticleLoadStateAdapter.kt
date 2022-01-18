package com.hesham.newsapp.ui.articles.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hesham.newsapp.R
import com.hesham.newsapp.databinding.ArticleLoadStateItemBinding

class ArticleLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<ArticleLoadStateAdapter.LoaderViewHolder>() {

    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderViewHolder {
        return LoaderViewHolder.create(parent, retry)
    }

    /**
     * view holder class for footer loader and error state handling
     */
    class LoaderViewHolder(val binding: ArticleLoadStateItemBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetry.setOnClickListener { retry.invoke() }
        }

        companion object {
            // get instance of the DoggoImageViewHolder
            fun create(parent: ViewGroup, retry: () -> Unit): LoaderViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.article_load_state_item, parent, false)
                val binding = ArticleLoadStateItemBinding.bind(view)
                return LoaderViewHolder(binding, retry)
            }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.textView.text = loadState.error.message
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.btnRetry.isVisible = loadState is LoadState.Error
            binding.textView.isVisible = loadState is LoadState.Error
        }
    }
}