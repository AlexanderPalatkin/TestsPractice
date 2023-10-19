package com.example.testspractice

import com.example.testspractice.model.SearchResponse
import com.example.testspractice.model.SearchResult
import com.example.testspractice.presenter.search.SearchPresenter
import com.example.testpractice.repository.GitHubRepository
import com.example.testspractice.view.search.ViewSearchContract
import com.nhaarman.mockito_kotlin.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import retrofit2.Response

class SearchPresenterTest {

    private lateinit var presenter: SearchPresenter

    @Mock
    private lateinit var repository: GitHubRepository

    @Mock
    private lateinit var viewContract: ViewSearchContract

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        //Создаем Презентер, используя моки Репозитория и Вью, проинициализированные строкой выше
        presenter = SearchPresenter(viewContract, repository)
    }

    @Test //Проверим вызов метода searchGitHub() у нашего Репозитория
    fun searchGitHub_Test() {
        val searchQuery = "some query"
        //Запускаем код, функционал которого хотим протестировать
        presenter.searchGitHub("some query")
        //Убеждаемся, что все работает как надо
        verify(repository, times(1)).searchGithub(searchQuery, presenter)
    }

    @Test //Проверяем работу метода handleGitHubError()
    fun handleGitHubError_Test() {
        //Вызываем у Презентера метод handleGitHubError()
        presenter.handleGitHubError()
        //Проверяем, что у viewContract вызывается метод displayError()
        verify(viewContract, times(1)).displayError()
    }

    //Проверяем работу метода handleGitHubResponse

    @Test //Для начала проверим, как приходит ответ сервера
    fun handleGitHubResponse_ResponseUnsuccessful() {
        //Создаем мок ответа сервера с типом Response<SearchResponse?>?
        val response = mock<Response<SearchResponse?>>()
        //Описываем правило, что при вызове метода isSuccessful должен возвращаться false
        whenever(response.isSuccessful).thenReturn(false)
        //Убеждаемся, что ответ действительно false
        assertFalse(response.isSuccessful)
    }

    @Test //Теперь проверим, как у нас обрабатываются ошибки
    fun handleGitHubResponse_Failure() {
        //Создаем мок ответа сервера с типом Response<SearchResponse?>?
        val response = mock<Response<SearchResponse?>>()
        //Описываем правило, что при вызове метода isSuccessful должен возвращаться false
        //В таком случае должен вызываться метод viewContract.displayError(...)
        whenever(response.isSuccessful).thenReturn(false)

        //Вызывваем у Презентера метод handleGitHubResponse()
        presenter.handleGitHubResponse(response)

        //Убеждаемся, что вызывается верный метод: viewContract.displayError("Response is null or unsuccessful"), и что он вызывается единожды
        verify(viewContract, times(1))
            .displayError("Response is null or unsuccessful")
    }

    @Test //Проверим порядок вызова методов viewContract
    fun handleGitHubResponse_ResponseFailure_ViewContractMethodOrder() {
        val response = mock<Response<SearchResponse?>>()
        whenever(response.isSuccessful).thenReturn(false)
        presenter.handleGitHubResponse(response)

        //Определяем порядок вызова методов какого класса мы хотим проверить
        val inOrder = inOrder(viewContract)
        //Прописываем порядок вызова методов
        inOrder.verify(viewContract).displayLoading(false)
        inOrder.verify(viewContract).displayError("Response is null or unsuccessful")
    }

    @Test //Проверим пустой ответ сервера
    fun handleGitHubResponse_ResponseIsEmpty() {
        val response = mock<Response<SearchResponse?>>()
        whenever(response.body()).thenReturn(null)
        //Вызываем handleGitHubResponse()
        presenter.handleGitHubResponse(response)
        //Убеждаемся, что body действительно null
        assertNull(response.body())
    }

    @Test //Теперь проверим непустой ответ сервера
    fun handleGitHubResponse_ResponseIsNotEmpty() {
        val response = mock<Response<SearchResponse?>>()
        whenever(response.body()).thenReturn(mock<SearchResponse>())
        //Вызываем handleGitHubResponse()
        presenter.handleGitHubResponse(response)
        //Убеждаемся, что body действительно null
        assertNotNull(response.body())
    }

    @Test //Проверим как обрабатывается случай, если ответ от сервера пришел пустой
    fun handleGitHubResponse_EmptyResponse() {
        val response = mock<Response<SearchResponse?>>()
        //Устанавливаем правило, что ответ успешный
        whenever(response.isSuccessful).thenReturn(true)
        //При этом body ответа == null. В таком случае должен вызываться метод viewContract.displayError(...)
        whenever(response.body()).thenReturn(null)

        //Вызываем handleGitHubResponse()
        presenter.handleGitHubResponse(response)

        //Убеждаемся, что вызывается верный метод: viewContract.displayError("Search results or total count are null"), и что он вызывается единожды
        verify(viewContract, times(1))
            .displayError("Search results or total count are null")
    }

    @Test //Пришло время проверить успешный ответ, так как все остальные случаи мы уже покрыли тестами
    fun handleGitHubResponse_Success() {
        //Мокаем ответ
        val response = mock<Response<SearchResponse?>>()
        //Мокаем тело ответа
        val searchResponse = mock<SearchResponse>()
        //Мокаем список
        val searchResults = listOf(mock<SearchResult>())

        //Прописываем правила
        //Когда ответ приходит, возвращаем response.isSuccessful == true и остальные данные в процессе выполнения метода handleGitHubResponse
        whenever(response.isSuccessful).thenReturn(true)
        whenever(response.body()).thenReturn(searchResponse)
        whenever(searchResponse.searchResults).thenReturn(searchResults)
        whenever(searchResponse.totalCount).thenReturn(101)

        //Запускаем выполнение метода
        presenter.handleGitHubResponse(response)

        //Убеждаемся, что ответ от сервера обрабатывается корректно
        verify(viewContract, times(1)).displaySearchResults(searchResults, 101)
    }

    @Test
    fun onAttach_Reflection_Test() {
        presenter.onDetach()

        // Проверяем, что viewContract равно null перед отсоединением
        assertNull(getPrivateField(presenter, "viewContract"))

        presenter.onAttach(viewContract)

        // Проверяем, что viewContract не равно null после отсоединения
        assertNotNull(getPrivateField(presenter, "viewContract"))
        assertEquals(viewContract, getPrivateField(presenter, "viewContract"))
    }

    @Test
    fun onDetach_Reflection_Test() {
        // Проверяем, что viewContract не равно null перед отсоединением
        assertEquals(viewContract, getPrivateField(presenter, "viewContract"))

        presenter.onDetach()

        // Проверяем, что viewContract становится равным null после отсоединения
        assertEquals(null, getPrivateField(presenter, "viewContract"))
    }

    @Test
    fun onAttach_Test() {
        presenter.onAttach(viewContract)
        verify(viewContract, times(1)).onAttached()
    }

    @Test
    fun onDetach_Test() {
        presenter.onDetach()
        verify(viewContract, never()).onAttached()
    }

    @After
    fun close() {
        presenter.onDetach()
    }
}