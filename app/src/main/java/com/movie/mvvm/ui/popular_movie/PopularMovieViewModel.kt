package com.movie.mvvm.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.movie.mvvm.data.repository.NetworkState
import com.movie.mvvm.data.repository.PopularMovieRepository
import com.movie.mvvm.data.vo.PopularMovie
import io.reactivex.disposables.CompositeDisposable

class PopularMovieViewModel(popularMovieRepository: PopularMovieRepository): ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    val popularMovie: LiveData<PagedList<PopularMovie>> by lazy {
        popularMovieRepository.fetchPopularMovie(compositeDisposable)
    }

    val networkState: LiveData<NetworkState> by lazy {
        popularMovieRepository.getPopularMovieNetworkState()
    }

    fun listIsEmpty(): Boolean {
        return popularMovie.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}