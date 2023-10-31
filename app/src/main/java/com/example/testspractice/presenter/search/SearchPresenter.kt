package com.example.testspractice.presenter.search

import com.example.testspractice.model.SearchResponse
import com.example.testspractice.presenter.RepositoryContract
import com.example.testspractice.presenter.SchedulerProvider
import com.example.testspractice.repository.RepositoryCallback
import com.example.testspractice.view.main.ViewContract
import com.example.testspractice.view.search.ViewSearchContract
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import retrofit2.Response

internal class SearchPresenter internal constructor(
    private var viewContract: ViewSearchContract?,
    private val repository: RepositoryContract,
    private val appSchedulerProvider: SchedulerProvider = SearchSchedulerProvider()
) : PresenterSearchContract, RepositoryCallback {

    override fun searchGitHub(searchQuery: String) {

        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            repository.searchGithub(searchQuery)
                .subscribeOn(appSchedulerProvider.io())
                .observeOn(appSchedulerProvider.ui())
                .doOnSubscribe { viewContract?.displayLoading(true) }
                .doOnTerminate { viewContract?.displayLoading(false) }
                .subscribeWith(object : DisposableObserver<SearchResponse>() {
                    override fun onNext(searchResponse: SearchResponse) {
                        val searchResults = searchResponse.searchResults
                        val totalCount = searchResponse.totalCount
                        if (searchResults != null && totalCount != null) {
                            viewContract?.displaySearchResults(
                                searchResults,
                                totalCount
                            )
                        } else {
                            viewContract?.displayError(
                                "Search results or total count are null")
                        }
                    }

                    override fun onError(e: Throwable) {
                        viewContract?.displayError(
                            e.message ?: "Response is null or unsuccessful")
                    }

                    override fun onComplete() {}
                }
                )
        )
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