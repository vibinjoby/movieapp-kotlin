package com.movie.mvvm.data.repository

import androidx.lifecycle.LiveData
import com.movie.mvvm.data.api.TheMovieDBInterface
import com.movie.mvvm.data.repository.MovieDetailsNetworkSource
import com.movie.mvvm.data.repository.NetworkState
import com.movie.mvvm.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService: TheMovieDBInterface) {
    private lateinit var movieDetailsNetworkSource: MovieDetailsNetworkSource

    fun fetchSingleMovieDetails(compositeDisposable: CompositeDisposable, movieId: Int): LiveData<MovieDetails> {
        movieDetailsNetworkSource = MovieDetailsNetworkSource(apiService,compositeDisposable)
        movieDetailsNetworkSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkSource.movieDetailsResponse
    }

    fun getMovieDetailsNetworkState(): LiveData<NetworkState> {
        return movieDetailsNetworkSource.networkState
    }
}