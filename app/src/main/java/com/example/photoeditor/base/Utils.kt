package com.example.photoeditor.base

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.view.View
import android.widget.ImageView
import androidx.annotation.DimenRes
import com.bumptech.glide.Glide
import com.example.photoeditor.R


const val BASE_URL = "https://lyrebirdstudio.s3-us-west-2.amazonaws.com/"

fun loadImage(view: View, imageView: ImageView, path: String?) {
    path?.let {
        Glide.with(view)
            .asBitmap()
            .load(path)
            .override(600, 600)
            .error(R.drawable.placeholder)
            .into(imageView)
    }
}

fun Bitmap.addOverlay(@DimenRes marginTop: Int, @DimenRes marginLeft: Int, overlay: Bitmap, resources: Resources): Bitmap? {
    val bitmapWidth = this.width
    val bitmapHeight = this.height
    val marginLeft = overlay.width.times(0.5) - overlay.width - resources.getDimension(marginLeft)
    val finalBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, this.config)
    val canvas = Canvas(finalBitmap)

    canvas.drawBitmap(this, Matrix(), null)
    canvas.drawBitmap(finalBitmap, marginLeft.toFloat(), resources.getDimension(marginTop), null)
    return finalBitmap
}