package com.example.photoeditor.feature.home.presentation

import com.example.photoeditor.feature.home.data.response.OverlayResponse

interface OverlaySelectedItemListener {
    fun onItemSelected(selectedOverlay: OverlayResponse)
}