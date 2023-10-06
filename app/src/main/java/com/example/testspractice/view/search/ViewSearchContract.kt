package com.example.testspractice.view.search

import com.example.testspractice.model.SearchResult
import com.example.testspractice.view.main.ViewContract

internal interface ViewSearchContract : ViewContract {
    fun displaySearchResults(
        searchResults: List<SearchResult>,
        totalCount: Int
    )

    fun displayError()
    fun displayError(error: String)
    fun displayLoading(show: Boolean)
}