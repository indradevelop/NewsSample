package com.example.news.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.news.api.NetworkResult
import com.example.news.model.NewsResult
import com.example.news.model.SourceResult
import com.example.news.repo.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject internal constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private var keyword = MutableStateFlow(value = String())
    fun setKeyword(params:String){
        viewModelScope.launch {
            keyword.emit(params)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getNewsPagingData(params: HashMap<String, String>): Flow<PagingData<NewsResult.Article>> {
        return keyword.flatMapLatest { word ->
            if(word.isNotEmpty()){
                params["q"] = word
            }
            if(params.isNotEmpty()) {
                repository.getNewsPagingApi(viewModelScope, params)
            }else {
                flow { }
            }
        }
    }

    fun getSourceData(params: HashMap<String, String>): LiveData<NetworkResult<List<SourceResult.Source?>>> {
        return liveData {

            val result = repository.getSourceApi(params)
            if (result.data != null) {

                if (result.data.status == "ok") {
                    emit(NetworkResult.Success(result.data.sources))
                } else {
                    emit(NetworkResult.Error(result.data.message))
                }

            } else {
                emit(NetworkResult.Error(result.message))
            }

        }
    }
}