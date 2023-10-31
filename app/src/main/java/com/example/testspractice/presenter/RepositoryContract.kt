package com.example.testspractice.presenter

import com.example.testspractice.model.SearchResponse
import com.example.testspractice.repository.RepositoryCallback
import io.reactivex.Observable

interface RepositoryContract {
    fun searchGithub(
        query: String,
        callback: RepositoryCallback
    )

    fun searchGithub(
        query: String
    ): Observable<SearchResponse>

    suspend fun searchGithubAsync(
        query: String
    ): SearchResponse
}