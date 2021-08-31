package com.example.photoeditor.feature.home.data.repo.datasource

import com.example.photoeditor.feature.home.data.HomeApiService
import javax.inject.Inject

class HomeRemoteDataSource @Inject constructor(
    private val service: HomeApiService
) {
    suspend fun getOverlayData() = service.getOverlayData()
}