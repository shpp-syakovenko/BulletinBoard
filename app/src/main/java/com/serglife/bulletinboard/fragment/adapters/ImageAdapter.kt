package com.serglife.bulletinboard.fragment.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.databinding.ImageAdapterItemBinding

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    val list = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_adapter_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateAdapter(newList: List<String>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ImageAdapterItemBinding.bind(view)
        fun bind(uri: String) {
            binding.imageItem.setImageURI(Uri.parse(uri))
        }
    }
}