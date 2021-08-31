package com.example.photoeditor.feature.home.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.photoeditor.R
import com.example.photoeditor.base.loadImage
import com.example.photoeditor.databinding.OverlayItemViewBinding
import com.example.photoeditor.feature.home.data.response.OverlayResponse

class OverlayListAdapter(
    private var overlayList: List<OverlayResponse>,
    private var overlaySelectedItemListener: OverlaySelectedItemListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private lateinit var binding: OverlayItemViewBinding

    class OverlayListViewHolder(val binding: OverlayItemViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater, R.layout.overlay_item_view, parent, false)
        return OverlayListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OverlayListViewHolder) {
            holder.binding.run {
                name.text = overlayList[position].overlayName
                val imageUrl = overlayList[position].overlayPreviewIconUrl
                loadImage(root, image, imageUrl)
                container.setOnClickListener {
                    overlaySelectedItemListener.onItemSelected(overlayList[position])
                }
            }
        }
    }

    override fun getItemCount(): Int = overlayList.size
}