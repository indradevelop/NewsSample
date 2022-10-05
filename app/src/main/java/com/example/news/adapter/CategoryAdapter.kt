package com.example.news.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.news.databinding.ItemCategoryBinding
import com.example.news.model.CategoryResult
import com.example.news.ui.SourceActivity

class CategoryAdapter :
    ListAdapter<CategoryResult, RecyclerView.ViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemCategoryBinding.inflate(
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
        private val binding: ItemCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.item?.let { result ->
                    val intent = Intent(binding.root.context, SourceActivity::class.java)
                    intent.putExtra(SourceActivity.CATEGORY, result.name)
                    binding.root.context.startActivity(intent)
                }
            }
        }

        fun bind(result: CategoryResult) {
            binding.apply {
                item = result
                executePendingBindings()
            }
        }
    }


    private class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryResult>() {

        override fun areItemsTheSame(oldResult: CategoryResult, newResult: CategoryResult): Boolean {
            return oldResult.id == newResult.id
        }

        override fun areContentsTheSame(oldResult: CategoryResult, newResult: CategoryResult): Boolean {
            return oldResult == newResult
        }
    }
}
