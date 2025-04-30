package com.vrolnes.tvtechnicalchallenge.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
// import com.vrolnes.tvtechnicalchallenge.BuildConfig // No longer needed
import com.vrolnes.tvtechnicalchallenge.data.remote.TmdbApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

const val API_KEY_NAME = "apiKey"
const val BASE_URL_NAME = "baseUrl"
const val IMAGE_BASE_URL_NAME = "imageBaseUrl"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // IMPORTANT: Replace this placeholder with your actual TMDB API Key before committing/running!
    private const val TMDB_API_KEY_PLACEHOLDER = "" //TODO DO NOT PUSH THE KEY

    @Provides
    @Singleton
    @Named(API_KEY_NAME)
    fun provideApiKey(): String = TMDB_API_KEY_PLACEHOLDER

    @Provides
    @Singleton
    @Named(BASE_URL_NAME)
    fun provideBaseUrl(): String = "https://api.themoviedb.org/3/"

    @Provides
    @Singleton
    @Named(IMAGE_BASE_URL_NAME)
    fun provideImageBaseUrl(): String = "https://image.tmdb.org/t/p/"

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()

         val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(loggingInterceptor)

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson,
        @Named(BASE_URL_NAME) baseUrl: String
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideTmdbApiService(retrofit: Retrofit): TmdbApiService {
        return retrofit.create(TmdbApiService::class.java)
    }
} 