package com.serglife.bulletinboard.fragment.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.databinding.SelectImageFragmentItemBinding
import com.serglife.bulletinboard.fragment.common.SelectImageItem

class SelectImageRVAdapter : RecyclerView.Adapter<SelectImageRVAdapter.ImageHolder>() {

    val list = mutableListOf<SelectImageItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_image_fragment_item, parent, false)
        return ImageHolder(view)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateAdapter(newList: List<SelectImageItem>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    class ImageHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = SelectImageFragmentItemBinding.bind(itemView)
        fun bind(item: SelectImageItem) {
            binding.tvTitleImage.text = item.title
            binding.imageContentItem.setImageURI(Uri.parse(item.imageUri))
        }
    }
}