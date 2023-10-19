package com.example.testpractice.main

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testspractice.R
import com.example.testspractice.databinding.ActivityMainBinding
import com.example.testspractice.model.SearchResult
import com.example.testspractice.presenter.RepositoryContract
import com.example.testspractice.presenter.search.PresenterSearchContract
import com.example.testspractice.presenter.search.SearchPresenter
import com.example.testspractice.repository.GitHubApi
import com.example.testpractice.repository.GitHubRepository
import com.example.testspractice.view.details.DetailsActivity
import com.example.testspractice.view.search.SearchResultAdapter
import com.example.testspractice.view.search.ViewSearchContract
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.*

class MainActivity : AppCompatActivity(), ViewSearchContract {
    private lateinit var binding: ActivityMainBinding

    private val adapter = SearchResultAdapter()

    private val presenter: PresenterSearchContract = SearchPresenter(this, createRepository())

    private var totalCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter.onAttach(this)
        setUI()
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }

    private fun setUI() {
        binding.toDetailsActivityButton.setOnClickListener {
            startActivity(DetailsActivity.getIntent(this, totalCount))
        }
        setQueryListener()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter
    }

    private fun setQueryListener() {
        binding.searchEditText.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchEditText.text.toString()
                if (query.isNotBlank()) {
                    presenter.searchGitHub(query)
                    return@OnEditorActionListener true
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.enter_search_word),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@OnEditorActionListener false
                }
            }
            false
        })
    }

    private fun createRepository(): RepositoryContract {
        return GitHubRepository()
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun displaySearchResults(
        searchResults: List<SearchResult>,
        totalCount: Int
    ) {
        with(binding.totalCountTextView) {
            visibility = View.VISIBLE
            text = String.format(
                Locale.getDefault(), getString(R.string.results_count),
                totalCount
            )
        }
        this.totalCount = totalCount
        adapter.updateResults(searchResults)
    }

    override fun displayError() {
        Toast.makeText(this, getString(R.string.undefined_error), Toast.LENGTH_SHORT).show()
    }

    override fun displayError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    override fun displayLoading(show: Boolean) {
        if (show) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onAttached() {
        // This method does not require implementation.
    }

    companion object {
        const val BASE_URL = "https://api.github.com"
        const val FAKE = "FAKE"
    }
}