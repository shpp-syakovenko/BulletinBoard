package com.serglife.bulletinboard.fragment.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.serglife.bulletinboard.MainActivity
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.model.Ad
import com.serglife.bulletinboard.databinding.AdListItemBinding
import com.serglife.bulletinboard.ui.edit.EditAdsAct

class AdsRVAdapter(val activity: MainActivity) : RecyclerView.Adapter<AdsRVAdapter.AdViewHolder>() {

    val list = mutableListOf<Ad>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ad_list_item, parent, false)
        return AdViewHolder(view, activity)
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

    class AdViewHolder(val item: View, val activity: MainActivity) : RecyclerView.ViewHolder(item) {
        private val binding = AdListItemBinding.bind(item)

        fun bind(ad: Ad) = with(binding) {
            tvDescriptionCV.text = ad.description
            tvTitleCV.text = ad.title
            tvPriceCV.text = ad.price
            showEditPanel(isOwner(ad))
            ibEditAd.setOnClickListener(onClickEdit(ad))
        }

        private fun onClickEdit(ad: Ad): View.OnClickListener{
            return View.OnClickListener {
                val editIntent = Intent(activity, EditAdsAct::class.java).apply {
                    putExtra(MainActivity.EDIT_STATE, true)
                    putExtra(MainActivity.ADS_DATA, ad)
                }
                activity.startActivity(editIntent)
            }
        }



        private fun isOwner(ad: Ad): Boolean{
            return activity.mAuth.uid == ad.uid
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