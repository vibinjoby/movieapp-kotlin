package com.movie.mvvm.ui.popular_movie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.movie.mvvm.R
import com.movie.mvvm.data.api.TheMovieDBClient
import com.movie.mvvm.data.repository.PopularMovieRepository

class MainActivity : AppCompatActivity() {
    private lateinit var popularMovieViewModel: PopularMovieViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pageNumber = 1
        val apiService = TheMovieDBClient.getClient()
        val repository = PopularMovieRepository(apiService)
        popularMovieViewModel = ViewModelProvider(this, object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return PopularMovieViewModel(repository) as T
            }
        })[PopularMovieViewModel::class.java]

        popularMovieViewModel.popularMovie.observe(this, {
            //loadMovieList
        })

    }
}