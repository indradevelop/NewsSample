package com.example.news.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.news.databinding.ItemNewsBinding
import com.example.news.model.NewsResult
import com.example.news.ui.WebviewActivity

class NewsAdapter : PagingDataAdapter<NewsResult.Article, RecyclerView.ViewHolder>(NewsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as ViewHolder).bind(item)
    }

    class ViewHolder(
        private val binding: ItemNewsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.item?.let { result ->
                    val intent = Intent(binding.root.context, WebviewActivity::class.java)
                    intent.putExtra(WebviewActivity.URL, result.url)
                    intent.putExtra(WebviewActivity.TITLE, result.title)
                    binding.root.context.startActivity(intent)
                }
            }
        }

        fun bind(result: NewsResult.Article?) {
            binding.apply {
                item = result
                executePendingBindings()
            }
        }
    }

    private class NewsDiffCallback : DiffUtil.ItemCallback<NewsResult.Article>() {

        override fun areItemsTheSame(oldResult: NewsResult.Article, newResult: NewsResult.Article): Boolean {
            return oldResult.title == newResult.title
        }

        override fun areContentsTheSame(oldResult: NewsResult.Article, newResult: NewsResult.Article): Boolean {
            return oldResult == newResult
        }
    }
}
