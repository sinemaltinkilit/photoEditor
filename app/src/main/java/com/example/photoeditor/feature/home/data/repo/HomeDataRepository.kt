package com.example.photoeditor.feature.home.data.repo

import com.example.photoeditor.feature.home.data.repo.datasource.HomeRemoteDataSource
import javax.inject.Inject

class HomeDataRepository @Inject constructor(
    private val remoteDataSource: HomeRemoteDataSource
) {
    suspend fun getOverlayData() = remoteDataSource.getOverlayData()
}