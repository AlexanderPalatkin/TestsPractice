package com.example.testspractice.presenter.details

import com.example.testspractice.view.details.ViewDetailsContract
import com.example.testspractice.view.main.ViewContract

internal class DetailsPresenter internal constructor(
    private var viewContract: ViewDetailsContract?,
    private var count: Int = 0
) : PresenterDetailsContract {

    override fun setCounter(count: Int) {
        this.count = count
    }

    override fun onIncrement() {
        count++
        viewContract?.setCount(count)
    }

    override fun onDecrement() {
        count--
        viewContract?.setCount(count)
    }

    override fun onAttach(view: ViewContract) {
        viewContract = view as ViewDetailsContract
    }

    override fun onDetach() {
        viewContract = null
    }
}