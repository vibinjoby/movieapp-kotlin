package com.movie.mvvm.single_movie_details

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.movie.mvvm.data.api.POSTER_BASE_URL
import com.movie.mvvm.data.api.TheMovieDBClient
import com.movie.mvvm.data.repository.MovieDetailsRepository
import com.movie.mvvm.data.repository.NetworkState
import com.movie.mvvm.databinding.ActivitySingleMovieBinding
import java.text.NumberFormat
import java.util.*

class SingleMovie : AppCompatActivity() {

    private lateinit var binding: ActivitySingleMovieBinding

    private lateinit var viewModel: SingleMovieViewModel
    private lateinit var repository: MovieDetailsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieId = 299534
        binding = ActivitySingleMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService = TheMovieDBClient.getClient()
        repository = MovieDetailsRepository(apiService)
        viewModel = ViewModelProvider(this, object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SingleMovieViewModel(repository,movieId) as T
            }
        })[SingleMovieViewModel::class.java]

        viewModel.movieDetails.observe(this,{
            binding.movieTitle.text = it.title
            binding.movieTagline.text = it.tagline
            binding.movieReleaseDate.text = it.releaseDate
            binding.movieRating.text = it.rating.toString()
            binding.movieRuntime.text = it.runtime.toString() + " minutes"
            binding.movieOverview.text = it.overview

            val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
            binding.movieBudget.text = formatCurrency.format(it.budget)
            binding.movieRevenue.text = formatCurrency.format(it.revenue)

            val posterPathUrl = POSTER_BASE_URL + it.posterPath
            Glide.with(this)
                .load(posterPathUrl)
                .into(binding.moviePoster)
        })

        viewModel.networkState.observe(this) {
            binding.progressBar.visibility = if(it == NetworkState.LOADING) View.VISIBLE else View.GONE
            binding.errorText.visibility = if(it == NetworkState.ERROR) View.VISIBLE else View.GONE
        }



    }
}