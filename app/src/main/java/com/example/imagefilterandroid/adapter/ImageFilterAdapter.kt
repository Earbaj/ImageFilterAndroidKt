package com.example.imagefilterandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imagefilterandroid.data.ImageFilter
import com.example.imagefilterandroid.databinding.ItemContainerFilterBinding
import com.example.imagefilterandroid.listeners.ImageFilterListener

class ImageFilterAdapter(
    private val imageFilters: List<ImageFilter>,
    private val imageFilterListener: ImageFilterListener
) :
RecyclerView.Adapter<ImageFilterAdapter.ImageFilterViewHolder>() {

    inner class ImageFilterViewHolder(val binding: ItemContainerFilterBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFilterViewHolder {
        val binding = ItemContainerFilterBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return ImageFilterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return imageFilters.size
    }

    override fun onBindViewHolder(holder: ImageFilterViewHolder, position: Int) {
        with(holder){
            with(imageFilters[position]){
                binding.imageFilterPreview.setImageBitmap(filterPreview)
                binding.textFilterName.text = name
                binding.root.setOnClickListener{
                    imageFilterListener.onFilterSelected(this)
                }
            }
        }
    }


}