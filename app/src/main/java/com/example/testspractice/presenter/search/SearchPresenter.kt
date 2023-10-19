package com.example.testspractice.presenter.search

import com.example.testspractice.model.SearchResponse
import com.example.testspractice.presenter.RepositoryContract
import com.example.testspractice.repository.RepositoryCallback
import com.example.testspractice.view.main.ViewContract
import com.example.testspractice.view.search.ViewSearchContract
import retrofit2.Response

internal class SearchPresenter internal constructor(
    private var viewContract: ViewSearchContract?,
    private val repository: RepositoryContract
) : PresenterSearchContract, RepositoryCallback {

    override fun searchGitHub(searchQuery: String) {
        viewContract?.displayLoading(true)
        repository.searchGithub(searchQuery, this)
    }

    override fun onAttach(view: ViewContract) {
        viewContract = view as ViewSearchContract
        viewContract?.onAttached()
    }

    override fun onDetach() {
        viewContract = null
    }

    override fun handleGitHubResponse(response: Response<SearchResponse?>?) {
        viewContract?.displayLoading(false)
        if (response != null && response.isSuccessful) {
            val searchResponse = response.body()
            val searchResults = searchResponse?.searchResults
            val totalCount = searchResponse?.totalCount
            if (searchResults != null && totalCount != null) {
                viewContract?.displaySearchResults(
                    searchResults,
                    totalCount
                )
            } else {
                viewContract?.displayError("Search results or total count are null")
            }
        } else {
            viewContract?.displayError("Response is null or unsuccessful")
        }
    }

    override fun handleGitHubError() {
        viewContract?.displayLoading(false)
        viewContract?.displayError()
    }
}