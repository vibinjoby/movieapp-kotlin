package com.movie.mvvm.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.movie.mvvm.data.api.TheMovieDBInterface
import com.movie.mvvm.data.vo.PopularMovie
import io.reactivex.disposables.CompositeDisposable

class PopularMovieDataSourceFactory(private val apiService: TheMovieDBInterface, private val compositeDisposable: CompositeDisposable)
    : DataSource.Factory<Int, PopularMovie>(){
    val moviesLiveDataSource = MutableLiveData<PopularMovieDataSource>()

    override fun create(): DataSource<Int, PopularMovie> {
        val dataSource = PopularMovieDataSource(apiService,compositeDisposable)
        moviesLiveDataSource.postValue(dataSource)
        return dataSource
    }
}