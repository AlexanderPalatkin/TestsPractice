package com.example.testspractice.presenter

import com.example.testspractice.view.main.ViewContract

internal interface PresenterContract {
    fun onAttach(view: ViewContract)
    fun onDetach()
}