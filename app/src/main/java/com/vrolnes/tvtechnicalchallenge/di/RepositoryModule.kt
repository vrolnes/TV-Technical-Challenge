package com.vrolnes.tvtechnicalchallenge.di

import com.vrolnes.tvtechnicalchallenge.data.repository.MovieRepositoryImpl
import com.vrolnes.tvtechnicalchallenge.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(impl: MovieRepositoryImpl): MovieRepository
} 