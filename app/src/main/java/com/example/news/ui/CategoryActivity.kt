package com.example.news.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.news.R
import com.example.news.adapter.CategoryAdapter
import com.example.news.databinding.ActivityCategoryBinding
import com.example.news.model.CategoryResult
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category)

        initData()
    }

    private fun initData() {
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        binding.rvCategories.layoutManager = layoutManager


        val items = arrayListOf<CategoryResult>()
        items.add(CategoryResult(1, "Business"))
        items.add(CategoryResult(2, "Entertainment"))
        items.add(CategoryResult(3, "General"))
        items.add(CategoryResult(4, "Health"))
        items.add(CategoryResult(5, "Science"))
        items.add(CategoryResult(6, "Sports"))
        items.add(CategoryResult(7, "Technology"))

        val adapter = CategoryAdapter()
        binding.rvCategories.adapter = adapter


        adapter.submitList(items)
    }
}