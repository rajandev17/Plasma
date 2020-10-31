package com.rajankali.core.network

import com.rajankali.core.THE_MOVIE_DB_API_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {
    private const val API_KEY = THE_MOVIE_DB_API_KEY
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    const val THUMB_NAIL_PATH = "https://image.tmdb.org/t/p/w500/%s"
    const val POSTER_PATH = "https://image.tmdb.org/t/p/original/%s"

    @Provides
    @Singleton
    fun providesOkHttpClient(authInterceptor: Interceptor): OkHttpClient{
        return OkHttpClient.Builder().addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()
    }

    @Provides
    @Singleton
    fun providesAuthInterceptor(): Interceptor{
        return Interceptor {
            val newRequest = it.request()
                    .newBuilder()
                    .url(it.request().url().newBuilder().addQueryParameter("api_key", API_KEY).build()).build()
            it.proceed(newRequest)
        }
    }

    @Provides
    @Singleton
    fun providesRetrofitClient(okHttpClient: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Provides
    @Singleton
    fun providesMovieApi(retrofit: Retrofit): MovieApi{
        return retrofit.create(MovieApi::class.java)
    }
}