package com.movie.mvvm.data.api

import com.movie.mvvm.BuildConfig
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://api.themoviedb.org/3/"
const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342"

object TheMovieDBClient {
    fun getClient(): TheMovieDBInterface {

        val requestInterceptor = Interceptor { chain ->
            val url: HttpUrl = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("api_key", BuildConfig.API_KEY.trim())
                .build()

            val request: Request = chain.request()
                .newBuilder()
                .url(url)
                .build()
            chain.proceed(request)
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheMovieDBInterface::class.java)
    }
}