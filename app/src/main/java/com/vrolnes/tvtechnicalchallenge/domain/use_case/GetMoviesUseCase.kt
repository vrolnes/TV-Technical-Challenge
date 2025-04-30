package com.vrolnes.tvtechnicalchallenge.domain.use_case

import com.vrolnes.tvtechnicalchallenge.domain.model.Movie
import com.vrolnes.tvtechnicalchallenge.domain.repository.MovieRepository
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {

    /**
     * Invokes the use case to fetch movies for a specific page and sort order.
     * @param page The page number to fetch.
     * @param sortBy The sorting criteria.
     * @return A list of [Movie] objects.
     * @throws Exception if the underlying repository call fails.
     */
    suspend operator fun invoke(page: Int, sortBy: String): List<Movie> {
         return movieRepository.getMovies(page = page, sortBy = sortBy)
    }
} 