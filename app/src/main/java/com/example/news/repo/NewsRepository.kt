package com.example.news.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.news.api.AppConstants
import com.example.news.api.ErrorResult
import com.example.news.api.NetworkResult
import com.example.news.model.NewsResult
import com.example.news.model.SourceResult
import com.example.news.pagination.NewsPagingSource
import com.example.news.service.NewsService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(private val service: NewsService){

    fun getNewsPagingApi(
        viewModelScope: CoroutineScope,
        params: HashMap<String, String>
    ) : Flow<PagingData<NewsResult.Article>> {
        return Pager(
            PagingConfig(pageSize = 20)
        ) {
            NewsPagingSource(service,params)
        }.flow.cachedIn(viewModelScope)
    }


    suspend fun getSourceApi(params: HashMap<String, String>) : NetworkResult<SourceResult?> {
        var result: NetworkResult<SourceResult?>
        try{
            val call = service.getSource(params)
            result = if(call.isSuccessful) {
                NetworkResult.Success(call.body())
            }else{
                try {
                    val error = Gson().fromJson(call.errorBody()?.string(), ErrorResult::class.java)
                    NetworkResult.Error(error.message)
                }catch (ex: Throwable){
                    NetworkResult.Error(AppConstants.errorOccurred)
                }
            }

        } catch ( e: IOException){
            result = NetworkResult.Error(AppConstants.errorNoInternet)
        } catch (e:Throwable){
            result = NetworkResult.Error(e.message)
            e.printStackTrace()
        }

        return result
    }

}