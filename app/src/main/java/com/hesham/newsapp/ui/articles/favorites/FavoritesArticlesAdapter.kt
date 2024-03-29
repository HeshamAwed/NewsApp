package com.hesham.newsapp.ui.articles.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hesham.newsapp.R
import com.hesham.newsapp.databinding.ArticleItemListBinding
import com.hesham.newsapp.domain.entities.Article

class FavoritesArticlesAdapter(
    val itemClicked: (Article) -> Unit,
    val removeFromFavorite: (Article) -> Unit
) : ListAdapter<Article, FavoritesArticlesAdapter.ViewHolder>(
    DIFF_CALLBACK
) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { article ->
            holder.binding.card.setOnClickListener { itemClicked(article) }
            holder.binding.btnFavorite.setOnClickListener { removeFromFavorite(article) }
            holder.binding.btnFavorite.setImageResource(R.drawable.ic_favorite)
            holder.bind(article)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ArticleItemListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(var binding: ArticleItemListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Article) {
            binding.article = item
            binding.executePendingBindings()
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(
                oldArticle: Article,
                newArticle: Article
            ) = oldArticle.title == newArticle.title

            override fun areContentsTheSame(
                oldArticle: Article,
                newArticle: Article
            ) = oldArticle == newArticle
        }
    }


}