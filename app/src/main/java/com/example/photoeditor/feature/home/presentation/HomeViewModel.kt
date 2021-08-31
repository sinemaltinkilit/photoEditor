package com.example.photoeditor.feature.home.presentation

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.photoeditor.feature.home.data.repo.HomeDataRepository
import com.example.photoeditor.feature.home.data.response.OverlayResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeDataRepository
) : ViewModel() {

    var progressBarVisibilityObservable = ObservableField(View.GONE)

    private val overlayListToAdapter = MutableLiveData<List<OverlayResponse>?>()
    val overlayListToAdapterLiveData: LiveData<List<OverlayResponse>?> get() = overlayListToAdapter

    fun getOverlayData() {
        progressBarVisibilityObservable.set(View.VISIBLE)
        CoroutineScope(Dispatchers.IO).launch {
            val response = repository.getOverlayData()
            handleResponse(response)
            progressBarVisibilityObservable.set(View.GONE)
        }
    }

    private fun handleResponse(response: List<OverlayResponse>) {
        response.takeIf { !it.isNullOrEmpty() }?.run {
            overlayListToAdapter.postValue(response)
        }
    }
}