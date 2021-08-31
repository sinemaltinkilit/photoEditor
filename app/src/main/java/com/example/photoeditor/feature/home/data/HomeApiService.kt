package com.example.photoeditor.feature.home.data

import com.example.photoeditor.feature.home.data.response.OverlayResponse
import retrofit2.http.GET

interface HomeApiService {

    @GET("candidates/overlay.json")
    suspend fun getOverlayData() : List<OverlayResponse>
}