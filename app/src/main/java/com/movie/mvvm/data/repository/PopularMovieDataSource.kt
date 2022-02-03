package com.movie.mvvm.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.movie.mvvm.data.api.TheMovieDBInterface
import com.movie.mvvm.data.vo.PopularMovie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PopularMovieDataSource(private val apiService: TheMovieDBInterface, private val completeDisposable: CompositeDisposable)
    : PageKeyedDataSource<Int, PopularMovie>(){
    private val TAG = "PopularMovieNetworkSour"

    private var page  = 1

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState>
        get() = _networkState


    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PopularMovie>) {
        _networkState.postValue(NetworkState.LOADING)

        completeDisposable.add(
            apiService.getPopularMovie(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if(it.totalPages>= params.key) {
                        callback.onResult(it.results, params.key + 1)
                        _networkState.postValue(NetworkState.LOADED)
                    } else {
                        _networkState.postValue(NetworkState.ENDOFLIST)
                    }
                },{
                    _networkState.postValue(NetworkState.ERROR)
                    Log.e(TAG, it.toString())
                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PopularMovie>) {

    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PopularMovie>
    ) {
        _networkState.postValue(NetworkState.LOADING)

        completeDisposable.add(
            apiService.getPopularMovie(page)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    callback.onResult(it.results,null, page+1)
                    _networkState.postValue(NetworkState.LOADED)
                },{
                    _networkState.postValue(NetworkState.ERROR)
                    Log.e(TAG, it.toString())
                })
        )
    }
}