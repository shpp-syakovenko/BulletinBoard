package com.serglife.bulletinboard.fragment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.data.Ad
import com.serglife.bulletinboard.databinding.AdListItemBinding

class AdsRVAdapter : RecyclerView.Adapter<AdsRVAdapter.AdViewHolder>() {

    val list = mutableListOf<Ad>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ad_list_item, parent, false)
        return AdViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateAdapter(newList: List<Ad>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()

    }

    class AdViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
        private val binding = AdListItemBinding.bind(item)

        fun bind(ad: Ad) {
            binding.apply {
                tvDescriptionCV.text = ad.description
                tvPriceCV.text = ad.price
            }

        }

    }
}