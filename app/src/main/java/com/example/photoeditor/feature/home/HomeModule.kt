package com.example.photoeditor.feature.home

import com.example.photoeditor.feature.home.data.HomeApiService
import com.example.photoeditor.feature.home.data.repo.HomeDataRepository
import com.example.photoeditor.feature.home.data.repo.datasource.HomeRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
class HomeModule {

    @Provides
    fun provideHomeRemoteDataSource(retrofit: Retrofit) = HomeRemoteDataSource(retrofit.create(HomeApiService::class.java))

    @Provides
    fun provideHomeDataRepository(remoteDataSource: HomeRemoteDataSource) = HomeDataRepository(remoteDataSource)
}