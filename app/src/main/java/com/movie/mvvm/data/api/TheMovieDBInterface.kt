package com.movie.mvvm.data.api

import com.movie.mvvm.data.vo.MovieDetails
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface TheMovieDBInterface {
    //https://api.themoviedb.org/3/movie/popular?api_key=c0ffadc77053be5e0136d63b25ab143f&page=1
    //https://api.themoviedb.org/3/movie/299534?api_key=c0ffadc77053be5e0136d63b25ab143f
    //https://api.themoviedb.org/3/

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>

}