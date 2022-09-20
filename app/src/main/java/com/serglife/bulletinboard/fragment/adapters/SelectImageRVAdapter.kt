package com.serglife.bulletinboard.fragment.adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.databinding.SelectImageFragmentItemBinding
import com.serglife.bulletinboard.ui.edit.EditAdsAct
import com.serglife.bulletinboard.utils.AdapterCallback
import com.serglife.bulletinboard.utils.ImageManager
import com.serglife.bulletinboard.utils.ImagePiker
import com.serglife.bulletinboard.utils.ItemTouchMoveCallback

class SelectImageRVAdapter : RecyclerView.Adapter<SelectImageRVAdapter.ImageHolder>(), ItemTouchMoveCallback.ItemTouchAdapter {

    val list = mutableListOf<Bitmap>()
    var adapterCallback: AdapterCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_image_fragment_item, parent, false)
        return ImageHolder(view, parent.context, this)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateAdapter(newList: List<Bitmap>, needClear: Boolean){
        if(needClear) list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onMove(start: Int, finish: Int) {
        val targetItem = list[finish]
        list[finish] = list[start]
        list[start] = targetItem
        notifyItemMoved(start, finish)
    }

    override fun onClear() {
        notifyDataSetChanged()
    }

    class ImageHolder(val itemView: View, val context: Context,val adapter: SelectImageRVAdapter) : RecyclerView.ViewHolder(itemView) {

        private val binding = SelectImageFragmentItemBinding.bind(itemView)

        fun bind(bitmap: Bitmap) {
            binding.tvTitleImage.text = context.resources.getStringArray(R.array.title_array)[adapterPosition]
            ImageManager.chooseScaleType(binding.imageContentItem, bitmap)
            binding.imageContentItem.setImageBitmap(bitmap)
            binding.imEditButton.setOnClickListener{
                ImagePiker.getSingleImage(context as EditAdsAct)
                context.editImagePos = adapterPosition
            }
            binding.imDeleteButton.setOnClickListener {
                adapter.list.removeAt(adapterPosition)
                adapter.notifyItemRemoved(adapterPosition)
                for(i in 0 until adapter.list.size) adapter.notifyItemChanged(i)
                adapter.adapterCallback?.onItemDelete()
            }
        }
    }
}