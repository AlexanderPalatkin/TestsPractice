package com.example.testspractice

import com.example.testspractice.presenter.details.DetailsPresenter
import com.example.testspractice.view.details.ViewDetailsContract
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.random.Random

class DetailsPresenterTest {

    private lateinit var presenter: DetailsPresenter

    private var paramCount = 0

    @Mock
    private lateinit var viewContract: ViewDetailsContract

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        presenter = DetailsPresenter(viewContract, paramCount)
    }

    @Test
    fun setCounter_Test() {
        val count = Random.nextInt()
        presenter.setCounter(count)

        assertEquals(count, getPrivateField(presenter, "count"))
    }

    @Test
    fun onIncrement() {
        presenter.onIncrement()

        assertEquals(1, getPrivateField(presenter, "count"))
        verify(viewContract, times(1)).setCount(1)
    }

    @Test
    fun onDecrement_Test() {
        presenter.onDecrement()

        assertEquals(-1, getPrivateField(presenter, "count"))
        verify(viewContract, times(1)).setCount(-1)
    }

    @Test
    fun onAttach_Test() {
        presenter.onDetach()

        assertNull(getPrivateField(presenter, "viewContract"))

        presenter.onAttach(viewContract)

        assertNotNull(getPrivateField(presenter, "viewContract"))
        assertEquals(viewContract, getPrivateField(presenter, "viewContract"))
    }

    @Test
    fun onDetach_Test() {
        assertEquals(viewContract, getPrivateField(presenter, "viewContract"))

        presenter.onDetach()

        assertEquals(null, getPrivateField(presenter, "viewContract"))
    }

    @After
    fun close() {
        presenter.onDetach()
    }
}