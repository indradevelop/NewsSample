package com.example.news.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.news.api.AppConstants
import com.example.news.api.ErrorResult
import com.example.news.model.NewsResult
import com.example.news.service.NewsService
import com.google.gson.Gson
import java.io.IOException

class NewsPagingSource(private val service: NewsService, private val query: HashMap<String, String>) : PagingSource<Int, NewsResult.Article>() {

    override fun getRefreshKey(state: PagingState<Int, NewsResult.Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsResult.Article> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            query["page"] = "$nextPageNumber"
            query["pageSize"] = "20"
            val response = service.getEverything(query)


            return if(response.isSuccessful) {
                val item = response.body()
                val articles = item?.articles

                var nextPageResponse:Int? = nextPageNumber.plus(1)
                if((articles?.size ?: 0) < 20){
                    nextPageResponse = null
                }

                LoadResult.Page(
                    data = articles ?: arrayListOf(),
                    prevKey = null, // Only paging forward.
                    nextKey = nextPageResponse
                )


            }else{
                try {
                    val error = Gson().fromJson(response.errorBody()?.string(), ErrorResult::class.java)
                    LoadResult.Error(Exception(error.message))
                }catch (ex: Exception){
                    LoadResult.Error(Exception(AppConstants.errorOccurred))
                }
            }
        }catch (e: IOException){
            return LoadResult.Error(Exception(AppConstants.errorNoInternet))
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}