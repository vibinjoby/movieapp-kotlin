package com.movie.mvvm.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.movie.mvvm.data.api.TheMovieDBInterface
import com.movie.mvvm.data.vo.PopularMovie
import io.reactivex.disposables.CompositeDisposable

class PopularMovieRepository(private val apiService: TheMovieDBInterface) {
    private lateinit var moviePagedList: LiveData<PagedList<PopularMovie>>
    private lateinit var moviesDataSourceFactory: PopularMovieDataSourceFactory

    fun fetchPopularMovie(completeDisposable: CompositeDisposable): LiveData<PagedList<PopularMovie>> {
        moviesDataSourceFactory = PopularMovieDataSourceFactory(apiService, completeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(20)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()
        return  moviePagedList
    }

    fun getPopularMovieNetworkState(): LiveData<NetworkState> {
        return  Transformations.switchMap(
            moviesDataSourceFactory.moviesLiveDataSource, PopularMovieDataSource::networkState)
    }
}