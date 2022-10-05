package com.example.news.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.news.R
import com.example.news.adapter.NewsAdapter
import com.example.news.databinding.ActivityNewsBinding
import com.example.news.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private val newsViewModel: NewsViewModel by viewModels()

    private var source: String? = null
    private var sourceName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news)
        source = intent.getStringExtra(SOURCE)
        sourceName = intent.getStringExtra(SOURCE_NAME)


        initToolbar()
        initData()
    }

    private fun initData() {
        val adapter = NewsAdapter()
        binding.rvNews.adapter = adapter

        binding.swNews.setOnRefreshListener {
            adapter.refresh()
        }

        lifecycleScope.launch(Dispatchers.Main) {
            binding.swNews.isRefreshing = true

            val params = hashMapOf<String, String>()
            source?.let {
                params["sources"] = it
            }

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
        sourceName?.let {
            binding.toolbar.title = it
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.toolbar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.menu_search -> {
                    val intent = Intent(this, SearchActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left)
                }
            }
            true
        }
    }

    companion object {
        const val SOURCE = "SOURCE"
        const val SOURCE_NAME = "SOURCE_NAME"
    }
}