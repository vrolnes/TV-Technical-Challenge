package com.vrolnes.tvtechnicalchallenge.domain.repository

import com.vrolnes.tvtechnicalchallenge.domain.model.Movie

interface MovieRepository {

    /**
     * Fetches a list of movies for a specific page and sorting criteria.
     * @param page The page number to fetch.
     * @param sortBy The sorting criteria (e.g., "popularity.desc").
     * @return A list of [Movie] objects.
     * @throws Exception if the network request fails.
     */
    suspend fun getMovies(page: Int, sortBy: String): List<Movie>

}