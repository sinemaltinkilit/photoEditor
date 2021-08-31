package com.example.photoeditor.component.customview

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.databinding.BindingAdapter
import com.example.photoeditor.R

@BindingAdapter("viewData")
fun CustomView.setData(data: CustomViewData?) {
    viewModel.handleData(data)

}

class CustomView @JvmOverloads constructor(
    ctx: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(ctx, attrs, defStyleAttr) {

    private var canvas: Canvas? = null
    var bitmap1Width: Float? = null
    var bitmap1Height: Float? = null

    var viewModel: CustomViewModel = CustomViewModel()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        this.canvas = canvas

        val res: Resources? = resources
        val drawable: Drawable? = res?.getDrawable(R.drawable.people)
        val bmp = (drawable as BitmapDrawable).bitmap

        bitmap1Width = bmp?.width?.toFloat()
        bitmap1Height = bmp?.height?.toFloat()

        canvas?.also {
            it.drawBitmap(bmp, 0f, bitmap1Height ?: 0f, null)
        }
    }

    fun onClickOverlay(bitmapOverlay: Bitmap?) {
        val paint = Paint()
        paint.alpha = 100
        canvas?.also {
            it.scale(10f, 2f, 10f, 2f)
            bitmapOverlay?.let { bitmap -> it.drawBitmap(bitmap, 0f, 0f, paint) }
        }
    }
}