package com.vrolnes.tvtechnicalchallenge.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vrolnes.tvtechnicalchallenge.domain.model.Movie
import com.vrolnes.tvtechnicalchallenge.domain.use_case.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MovieListState())
    val state: StateFlow<MovieListState> = _state.asStateFlow()

    // TMDB Sort constants
    object SortBy {
        const val POPULARITY_DESC = "popularity.desc"
        const val VOTE_AVERAGE_DESC = "vote_average.desc"
        const val REVENUE_DESC = "revenue.desc"
    }

    init {
        _state.update {
            it.copy(isPopularLoading = true, isTopRatedLoading = true, isRevenueLoading = true)
        }
        loadMoviesForCategory(SortBy.POPULARITY_DESC, isInitialLoad = true)
        loadMoviesForCategory(SortBy.VOTE_AVERAGE_DESC, isInitialLoad = true)
        loadMoviesForCategory(SortBy.REVENUE_DESC, isInitialLoad = true)
    }

    /**
     * Loads the next page of movies for a specific category.
     */
    fun loadNextPageForCategory(sortBy: String) {
        loadMoviesForCategory(sortBy, isInitialLoad = false)
    }

    private fun loadMoviesForCategory(sortBy: String, isInitialLoad: Boolean) {
        // Simplified Check: Return if already loading the *next* page for this category
        // or if the end has been reached for a non-initial load.
        val currentState = _state.value
        val isLoadingNext = when (sortBy) {
            SortBy.POPULARITY_DESC -> currentState.isPopularLoadingNextPage
            SortBy.VOTE_AVERAGE_DESC -> currentState.isTopRatedLoadingNextPage
            SortBy.REVENUE_DESC -> currentState.isRevenueLoadingNextPage
            else -> false
        }
        val endReached = when (sortBy) {
            SortBy.POPULARITY_DESC -> currentState.popularEndReached
            SortBy.VOTE_AVERAGE_DESC -> currentState.topRatedEndReached
            SortBy.REVENUE_DESC -> currentState.revenueEndReached
            else -> true // Prevent loading unknown category
        }

        if (isLoadingNext || (!isInitialLoad && endReached)) {
            return
        }

        viewModelScope.launch {
            val pageToLoad: Int
            val currentMovies: List<Movie>

            // Determine page and current movies list for the category
            when (sortBy) {
                SortBy.POPULARITY_DESC -> {
                    pageToLoad = if (isInitialLoad) 1 else currentState.popularCurrentPage
                    currentMovies = currentState.popularMovies
                }
                SortBy.VOTE_AVERAGE_DESC -> {
                    pageToLoad = if (isInitialLoad) 1 else currentState.topRatedCurrentPage
                    currentMovies = currentState.topRatedMovies
                }
                SortBy.REVENUE_DESC -> {
                    pageToLoad = if (isInitialLoad) 1 else currentState.revenueCurrentPage
                    currentMovies = currentState.revenueMovies
                }
                else -> {
                    return@launch
                }
            }


            // Update loading state for the specific category
            _state.update {
                when (sortBy) {
                    SortBy.POPULARITY_DESC -> it.copy(isPopularLoading = isInitialLoad, isPopularLoadingNextPage = !isInitialLoad, popularError = null)
                    SortBy.VOTE_AVERAGE_DESC -> it.copy(isTopRatedLoading = isInitialLoad, isTopRatedLoadingNextPage = !isInitialLoad, topRatedError = null)
                    SortBy.REVENUE_DESC -> it.copy(isRevenueLoading = isInitialLoad, isRevenueLoadingNextPage = !isInitialLoad, revenueError = null)
                    else -> it
                }
            }

            try {
                val newMovies = getMoviesUseCase(page = pageToLoad, sortBy = sortBy)

                val existingIds = currentMovies.map { it.id }.toSet()
                val uniqueNewMovies = newMovies.filter { it.id !in existingIds }

                // Update state with new data
                _state.update {
                    when (sortBy) {
                        SortBy.POPULARITY_DESC -> it.copy(
                            popularMovies = currentMovies + uniqueNewMovies,
                            popularCurrentPage = pageToLoad + 1,
                            isPopularLoading = false,
                            isPopularLoadingNextPage = false,
                            popularEndReached = newMovies.isEmpty()
                        )
                        SortBy.VOTE_AVERAGE_DESC -> it.copy(
                            topRatedMovies = currentMovies + uniqueNewMovies,
                            topRatedCurrentPage = pageToLoad + 1,
                            isTopRatedLoading = false,
                            isTopRatedLoadingNextPage = false,
                            topRatedEndReached = newMovies.isEmpty()
                        )
                        SortBy.REVENUE_DESC -> it.copy(
                            revenueMovies = currentMovies + uniqueNewMovies,
                            revenueCurrentPage = pageToLoad + 1,
                            isRevenueLoading = false,
                            isRevenueLoadingNextPage = false,
                            revenueEndReached = newMovies.isEmpty()
                        )
                        else -> it
                    }
                }

            } catch (e: Exception) {
                _state.update {
                    val errorMsg = e.message ?: "Failed to load movies"
                    when (sortBy) {
                        SortBy.POPULARITY_DESC -> it.copy(isPopularLoading = false, isPopularLoadingNextPage = false, popularError = errorMsg)
                        SortBy.VOTE_AVERAGE_DESC -> it.copy(isTopRatedLoading = false, isTopRatedLoadingNextPage = false, topRatedError = errorMsg)
                        SortBy.REVENUE_DESC -> it.copy(isRevenueLoading = false, isRevenueLoadingNextPage = false, revenueError = errorMsg)
                        else -> it
                    }
                }
            }
        }
    }
} 