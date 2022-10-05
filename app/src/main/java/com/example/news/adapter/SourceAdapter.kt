package com.example.news.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.news.databinding.ItemSourceBinding
import com.example.news.model.SourceResult
import com.example.news.ui.NewsActivity

class SourceAdapter : ListAdapter<SourceResult.Source, RecyclerView.ViewHolder>(SourceDiffCallback()), Filterable {
    private var list = mutableListOf<SourceResult.Source?>()

    fun setData(list: MutableList<SourceResult.Source?>){
        this.list = list
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemSourceBinding.inflate(
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
        private val binding: ItemSourceBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener {
                binding.item?.let { result ->
                    val intent = Intent(binding.root.context, NewsActivity::class.java);
                    intent.putExtra(NewsActivity.SOURCE, result.id)
                    intent.putExtra(NewsActivity.SOURCE_NAME, result.name)
                    binding.root.context.startActivity(intent)
                }
            }
        }

        fun bind(result: SourceResult.Source) {
            binding.apply {
                item = result
                executePendingBindings()
            }
        }
    }

    private class SourceDiffCallback : DiffUtil.ItemCallback<SourceResult.Source>() {

        override fun areItemsTheSame(oldResult: SourceResult.Source, newResult: SourceResult.Source): Boolean {
            return oldResult.id == newResult.id
        }

        override fun areContentsTheSame(oldResult: SourceResult.Source, newResult: SourceResult.Source): Boolean {
            return oldResult == newResult
        }
    }

    override fun getFilter(): Filter {
        return customFilter
    }

    private val customFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<SourceResult.Source?>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(list)
            } else {
                for (item in list) {
                    if (item?.name != null && item.name.lowercase().contains(constraint.toString().lowercase())) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            submitList(filterResults?.values as MutableList<SourceResult.Source>)
        }

    }
}
