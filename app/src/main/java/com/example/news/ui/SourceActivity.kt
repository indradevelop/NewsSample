package com.example.news.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.example.news.R
import com.example.news.adapter.SourceAdapter
import com.example.news.databinding.ActivitySourceBinding
import com.example.news.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SourceActivity : AppCompatActivity() {
    private lateinit var adapter: SourceAdapter
    private lateinit var binding: ActivitySourceBinding
    private val newsViewModel: NewsViewModel by viewModels()
    private var categoryName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_source)
        categoryName = intent.getStringExtra(CATEGORY)

        initToolbar()
        initData()
        initSearch()
    }

    private fun initSearch() {
        binding.edSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.filter.filter(p0)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }

    private fun initData() {

        binding.swSource.setOnRefreshListener {
            loadData()
        }

        adapter = SourceAdapter()
        binding.rvSource.adapter = adapter

        loadData()
    }

    private fun loadData() {
        val params = hashMapOf<String, String>()
        categoryName?.let {
            params["category"] =  it.lowercase()
        }
        newsViewModel.getSourceData(params).observe(this) {
            binding.swSource.isRefreshing = false
            if (it.data == null) {
                /*val toast = Toast.makeText(this, it.message, Toast.LENGTH_SHORT)
                with(toast) {
                    setGravity(Gravity.CENTER, 0, 0)
                    show()
                }*/
                binding.tvError.text = it.message
                binding.tvError.isVisible = true
            } else {
                adapter.setData(it.data.toMutableList())
            }
        }
    }

    private fun initToolbar() {
        categoryName?.let {
            binding.toolbar.title = it
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }


    companion object{
        const val CATEGORY = "CATEGORY"
    }
}