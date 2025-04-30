package com.vrolnes.tvtechnicalchallenge.data.repository

import com.vrolnes.tvtechnicalchallenge.data.mapper.toDomain
import com.vrolnes.tvtechnicalchallenge.data.remote.TmdbApiService
import com.vrolnes.tvtechnicalchallenge.di.API_KEY_NAME
import com.vrolnes.tvtechnicalchallenge.di.IMAGE_BASE_URL_NAME
import com.vrolnes.tvtechnicalchallenge.domain.model.Movie
import com.vrolnes.tvtechnicalchallenge.domain.repository.MovieRepository
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val apiService: TmdbApiService,
    @Named(IMAGE_BASE_URL_NAME) private val imageBaseUrl: String,
    @Named(API_KEY_NAME) private val apiKey: String
) : MovieRepository {

    override suspend fun getMovies(page: Int, sortBy: String): List<Movie> {
        return try {
            val response = apiService.discoverMovies(apiKey = apiKey, page = page, sortBy = sortBy)
            response.results.toDomain(imageBaseUrl = imageBaseUrl)
        } catch (e: Exception) {
            println("Error fetching $sortBy movies (page $page): ${e.message}")
            throw e
        }
    }
} 