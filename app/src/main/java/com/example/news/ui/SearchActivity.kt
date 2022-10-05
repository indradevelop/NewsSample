package com.example.news.ui

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.news.R
import com.example.news.adapter.NewsAdapter
import com.example.news.databinding.ActivitySearchBinding
import com.example.news.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private lateinit var adapter: NewsAdapter
    private lateinit var binding: ActivitySearchBinding
    private val newsViewModel: NewsViewModel by viewModels()

    private var isSearch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)


        initToolbar()
        initData()
        initSearch()
    }

    private fun initSearch() {
        binding.edSearch.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                doSearch()
            }
            true
        }
    }

    private fun doSearch() {
        isSearch = true
        newsViewModel.setKeyword(binding.edSearch.text.toString().trim())
    }

    private fun initData() {
        adapter = NewsAdapter()
        binding.rvNews.adapter = adapter

        binding.swNews.setOnRefreshListener {
            adapter.refresh()
        }

        lifecycleScope.launch(Dispatchers.Main) {
            val params = hashMapOf<String, String>()
            newsViewModel.getNewsPagingData(params).collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }

        showEmptyList(false)
        lifecycleScope.launch(Dispatchers.Main) {
            adapter.loadStateFlow.collectLatest { loadState ->
                if (loadState.refresh is LoadState.Error) {
                    binding.swNews.isRefreshing = false
                    showEmptyList(true, (loadState.refresh as LoadState.Error).error.message)
                } else if (loadState.refresh !is LoadState.Loading && adapter.itemCount < 1) {
                    binding.swNews.isRefreshing = false
                    showEmptyList(true, getString(R.string.no_data))
                } else if (loadState.refresh !is LoadState.Loading) {
                    binding.swNews.isRefreshing = false
                } else {
                    showEmptyList(false)
                }
            }
        }
    }


    private fun showEmptyList(show: Boolean, msg: String? = "") {
        binding.tvError.text = msg
        binding.tvError.isVisible = show
        binding.swNews.isVisible = !show

    }

    private fun initToolbar() {
        binding.toolbar.title = "Search"
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left,
            R.anim.slide_out_right)
    }
}