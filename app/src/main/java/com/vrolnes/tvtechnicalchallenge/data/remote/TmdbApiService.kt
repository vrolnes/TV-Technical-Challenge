package com.vrolnes.tvtechnicalchallenge.data.remote

import com.vrolnes.tvtechnicalchallenge.data.remote.dto.DiscoverResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApiService {

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String,
        @Query("language") language: String = "en-US"
    ): DiscoverResponseDto

}