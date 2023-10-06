package com.example.testspractice.presenter.search

import com.example.testspractice.presenter.PresenterContract

internal interface PresenterSearchContract: PresenterContract {
    fun searchGitHub(searchQuery: String)
    //onAttach
    //onDetach
}