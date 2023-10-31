package com.example.testpractice.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testpractice.main.MainActivity.Companion.BASE_URL
import com.example.testpractice.repository.GitHubRepository
import com.example.testspractice.model.SearchResponse
import com.example.testspractice.presenter.RepositoryContract
import com.example.testspractice.repository.GitHubApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchViewModel(
    private val repository: RepositoryContract = GitHubRepository(
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(GitHubApi::class.java)
    )
) : ViewModel() {
    private val _liveData = MutableLiveData<ScreenState>()
    private val liveData: LiveData<ScreenState> = _liveData

    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable ->
            handleError(throwable)
        })

    fun subscribeToLiveData() = liveData

    fun searchGitHub(searchQuery: String) {
        _liveData.value = ScreenState.Loading
        viewModelCoroutineScope.launch {
            val searchResponse = repository.searchGithubAsync(searchQuery)
            val searchResults = searchResponse.searchResults
            val totalCount = searchResponse.totalCount
            if (searchResults != null && totalCount != null) {
                _liveData.value = ScreenState.Working(searchResponse)
            } else {
                _liveData.value =
                    ScreenState.Error(Throwable("Search results or total count are null"))
            }
        }
    }

    private fun handleError(error: Throwable) {
        _liveData.value =
            ScreenState.Error(
                Throwable(
                    error.message ?: "Response is null or unsuccessful"
                )
            )
    }

    override fun onCleared() {
        super.onCleared()
        viewModelCoroutineScope.coroutineContext.cancelChildren()
    }
}

sealed class ScreenState {
    object Loading : ScreenState()
    data class Working(val searchResponse: SearchResponse) : ScreenState()
    data class Error(val error: Throwable) : ScreenState()
}