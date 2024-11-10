package com.example.imagefilterandroid.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.imagefilterandroid.R
import com.example.imagefilterandroid.data.ImageFilter
import com.example.imagefilterandroid.databinding.ItemContainerFilterBinding
import com.example.imagefilterandroid.listeners.ImageFilterListener

class ImageFilterAdapter(
    private val imageFilters: List<ImageFilter>,
    private val imageFilterListener: ImageFilterListener
) :
RecyclerView.Adapter<ImageFilterAdapter.ImageFilterViewHolder>() {


    private var selectedFilterPosition = 0
    private  var previouslySelectedPosition = 0

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
        with(holder) {
            val currentPosition = holder.adapterPosition
            val currentFilter = imageFilters[currentPosition]

            with(currentFilter) {
                binding.imageFilterPreview.setImageBitmap(filterPreview)
                binding.textFilterName.text = name
                binding.root.setOnClickListener {
                    val selectedPosition = holder.adapterPosition
                    if (selectedPosition != selectedFilterPosition) {
                        imageFilterListener.onFilterSelected(this)
                        previouslySelectedPosition = selectedFilterPosition
                        selectedFilterPosition = selectedPosition
                        with(this@ImageFilterAdapter) {
                            notifyItemChanged(previouslySelectedPosition, Unit)
                            notifyItemChanged(selectedFilterPosition, Unit)
                        }
                    }
                }
            }

            binding.textFilterName.setTextColor(
                ContextCompat.getColor(
                    binding.textFilterName.context,
                    if (selectedFilterPosition == currentPosition) {
                        com.google.android.material.R.color.design_default_color_primary_dark
                    } else {
                        com.google.android.material.R.color.primary_text_default_material_light
                    }
                )
            )
        }
    }


//    override fun onBindViewHolder(holder: ImageFilterViewHolder, position: Int) {
//        with(holder){
//            with(imageFilters[position]){
//                binding.imageFilterPreview.setImageBitmap(filterPreview)
//                binding.textFilterName.text = name
//                binding.root.setOnClickListener{
//                    if(position != selectedFilterPosition){
//                        imageFilterListener.onFilterSelected(this)
//                        previouslySelectedPosition = selectedFilterPosition
//                        selectedFilterPosition = position
//                        with(this@ImageFilterAdapter){
//                            notifyItemChanged(previouslySelectedPosition,Unit)
//                            notifyItemChanged(selectedFilterPosition, Unit)
//                        }
//                    }
//
//                }
//            }
//            binding.textFilterName.setTextColor(
//                ContextCompat.getColor(
//                    binding.textFilterName.context,
//                    if(selectedFilterPosition == position) {
//                        com.google.android.material.R.color.design_default_color_primary_dark
//                    }else{
//                        com.google.android.material.R.color.primary_text_default_material_light
//                    }
//
//                )
//            )
//        }
//    }


}