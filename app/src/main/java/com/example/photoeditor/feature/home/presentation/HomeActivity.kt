package com.example.photoeditor.feature.home.presentation

import android.content.Context
import android.graphics.*
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.StrictMode
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.photoeditor.R
import com.example.photoeditor.databinding.ActivityHomeBinding
import com.example.photoeditor.feature.home.data.response.OverlayResponse
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection


@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityHomeBinding

    private var bmp2: Bitmap? = null
    private var canvas: Canvas? = null

    private var detector: GestureDetector? = null
    private var gestureListener: GestureDetector.SimpleOnGestureListener? = null

    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.viewModel = viewModel
        setActionbar()
        checkConnection().takeIf { it }?.let {
            initUI()
            observeViewModel()
            viewModel.getOverlayData()
        } ?: run {
            showErrorDialog(
                getString(R.string.network_error_title),
                getString(R.string.network_error_message)
            )
        }
    }

    private fun setActionbar() {
        val view = layoutInflater.inflate(R.layout.action_bar_view, null)
        val layoutParams = ActionBar.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER_HORIZONTAL
        )
        supportActionBar?.run {
            setCustomView(view, layoutParams)
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        val closeButton = supportActionBar?.customView?.findViewById(R.id.close_icon) as ImageView
        val saveButton = supportActionBar?.customView?.findViewById(R.id.save_icon) as ImageView

        closeButton.setOnClickListener {
            showErrorDialog(
                getString(R.string.close_app_warning_title),
                getString(R.string.close_app_warning_message),
                getString(
                    R.string.negative_button
                )
            )
        }
        saveButton.setOnClickListener {  }
    }

    private fun initUI() {
        this.canvas = Canvas()
        binding.run {

            detector = GestureDetector(this@HomeActivity, listener)

            customView.setOnTouchListener { v, event ->
                detector!!.onTouchEvent(event)
            }
        }
    }

    private var listener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            return super.onDown(e)
            true
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            overlayListToAdapterLiveData.observe(this@HomeActivity, { list ->
                list.takeIf { it.isNullOrEmpty() }?.let {
                    binding.overlayList.visibility = View.GONE
                } ?: run {
                    list?.let {
                        binding.overlayList.adapter = OverlayListAdapter(
                            it,
                            overlaySelectedItemListener
                        )
                    }
                }
            })
        }
    }

    fun createRequest(url: String?) {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val url = URL(url)
        val httpClient = url.openConnection() as HttpsURLConnection
        if (httpClient.responseCode == HttpsURLConnection.HTTP_OK) {
            try {
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = false

                val inputStream: InputStream = httpClient.inputStream
                bmp2 = BitmapFactory.decodeStream(inputStream, null, options)

                binding.customView.onClickOverlay(bmp2)
                binding.customView.invalidate()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                httpClient.disconnect()
            }
        } else {
            println("ERROR ${httpClient.responseCode}")
        }
    }

    private var overlaySelectedItemListener = object : OverlaySelectedItemListener {
        override fun onItemSelected(selectedOverlay: OverlayResponse) {
            runBlocking {
                val res = async { createRequest(selectedOverlay.overlayPreviewIconUrl) }
                res.await()
            }
        }

    }

    private fun checkConnection(): Boolean {
        val cm: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo?.isConnectedOrConnecting ?: false
    }

    override fun onPause() {
        mainScope.cancel()
        super.onPause()
    }

    private fun showErrorDialog(title: String, message: String, negativeButtonText: String? = null) {
        AlertDialog.Builder(this).run {
            setTitle(title)
            setMessage(message)
            setCancelable(false)
            setPositiveButton(getString(R.string.done_button)) { _, _ -> finish() }
            negativeButtonText?.let { setNegativeButton(negativeButtonText) { _, _ -> } }
        }?.show()
    }
}
