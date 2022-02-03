package com.movie.mvvm.ui.popular_movie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.movie.mvvm.R
import com.movie.mvvm.data.api.TheMovieDBClient
import com.movie.mvvm.data.repository.NetworkState
import com.movie.mvvm.data.repository.PopularMovieRepository

class MainActivity : AppCompatActivity() {
    private lateinit var popularMovieViewModel: PopularMovieViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService = TheMovieDBClient.getClient()
        val repository = PopularMovieRepository(apiService)
        popularMovieViewModel = ViewModelProvider(this, object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PopularMovieViewModel(repository) as T
            }
        })[PopularMovieViewModel::class.java]

        val movieAdapter = PopularMovieAdapter(this)
        val gridManager = GridLayoutManager(this,3)

        gridManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if(movieAdapter.getItemViewType(position) === movieAdapter.MOVIE_VIEW_TYPE) 1
                       else 3
            }
        }

        val recyclerView = findViewById<RecyclerView>(R.id.rv_movie_list)
        recyclerView.layoutManager = gridManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = movieAdapter

        popularMovieViewModel.popularMovie.observe(this, {
            movieAdapter.submitList(it)
        })

        val progressBar = findViewById<ProgressBar>(R.id.progress_bar_popular)
        val textErrorPopular = findViewById<TextView>(R.id.error_text_popular)

        popularMovieViewModel.networkState.observe(this, {
            progressBar.visibility = if(popularMovieViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            textErrorPopular.visibility = if(popularMovieViewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE

            if(!popularMovieViewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })
    }
}