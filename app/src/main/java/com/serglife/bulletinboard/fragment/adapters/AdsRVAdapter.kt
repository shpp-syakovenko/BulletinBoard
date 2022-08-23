package com.serglife.bulletinboard.fragment.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.model.Ad
import com.serglife.bulletinboard.databinding.AdListItemBinding

class AdsRVAdapter(val auth : FirebaseAuth) : RecyclerView.Adapter<AdsRVAdapter.AdViewHolder>() {

    val list = mutableListOf<Ad>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ad_list_item, parent, false)
        return AdViewHolder(view, auth)
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

    class AdViewHolder(val item: View, val auth: FirebaseAuth) : RecyclerView.ViewHolder(item) {
        private val binding = AdListItemBinding.bind(item)

        fun bind(ad: Ad) {
            binding.apply {
                tvDescriptionCV.text = ad.description
                tvTitleCV.text = ad.title
                tvPriceCV.text = ad.price
            }
            showEditPanel(isOwner(ad))
        }

        private fun isOwner(ad: Ad): Boolean{
            return auth.uid == ad.uid
        }

        private fun showEditPanel(isOwner: Boolean){
            if(isOwner){
                binding.editPanel.visibility = View.VISIBLE
            }else{
                binding.editPanel.visibility = View.GONE
            }
        }

    }
}