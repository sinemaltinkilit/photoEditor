package com.example.photoeditor.component.customview

import android.graphics.*
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.photoeditor.R

class CustomViewModel : ViewModel() {

    val imageViewObservable = ObservableField<Unit>()

    fun handleData(customData: CustomViewData?) {
        customData?.run {
            val bm = BitmapFactory.decodeResource(imageResource, R.drawable.people)
            //val image = customView.setImageBitmap(bm)
        }
    }
}