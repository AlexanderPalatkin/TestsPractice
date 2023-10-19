package com.example.testpractice.repository

import com.example.testspractice.model.SearchResponse
import com.example.testspractice.presenter.RepositoryContract
import com.example.testspractice.repository.RepositoryCallback
import retrofit2.Response

internal class GitHubRepository : RepositoryContract {
    override fun searchGithub(
        query: String,
        callback: RepositoryCallback
    ) {
        callback.handleGitHubResponse(Response.success(SearchResponse(42, listOf())))
    }
}