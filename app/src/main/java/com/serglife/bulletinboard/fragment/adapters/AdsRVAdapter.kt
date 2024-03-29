package com.serglife.bulletinboard.fragment.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.serglife.bulletinboard.MainActivity
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.model.Ad
import com.serglife.bulletinboard.databinding.AdListItemBinding
import com.serglife.bulletinboard.ui.edit.EditAdsAct
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

class AdsRVAdapter(val activity: MainActivity) : RecyclerView.Adapter<AdsRVAdapter.AdViewHolder>() {

    val list = mutableListOf<Ad>()
    private var timeFormatter: SimpleDateFormat? = null

    init {
        timeFormatter = SimpleDateFormat("dd/MM/yyyy - hh:mm:ss", Locale.getDefault())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ad_list_item, parent, false)
        return AdViewHolder(view, activity, timeFormatter!!)
    }

    override fun onBindViewHolder(holder: AdViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateAdapter(newList: List<Ad>) {
        val tempList = mutableListOf<Ad>()
        tempList.addAll(list)
        tempList.addAll(newList)
        val diffResult = DiffUtil.calculateDiff(DiffUtilHelper(list, tempList))
        diffResult.dispatchUpdatesTo(this)
        list.clear()
        list.addAll(tempList)
    }

    fun updateAdapterWithClear(newList: List<Ad>) {
        val diffResult = DiffUtil.calculateDiff(DiffUtilHelper(list, newList))
        diffResult.dispatchUpdatesTo(this)
        list.clear()
        list.addAll(newList)
    }

    class AdViewHolder(val item: View, val activity: MainActivity, val formatter: SimpleDateFormat) : RecyclerView.ViewHolder(item) {
        private val binding = AdListItemBinding.bind(item)

        fun bind(ad: Ad) = with(binding) {
            tvDescriptionCV.text = ad.description
            tvTitleCV.text = ad.title
            tvPriceCV.text = ad.price
            tvViewCounter.text = ad.viewsCounter
            tvFav.text = ad.favCounter
            (activity.getString(R.string.publishTime) + " ${getTimeFromMillions(ad.time)}").also { tvPublishTime.text = it }
            Picasso.get().load(ad.mainImage).into(imageView)
            isFav(ad)
            showEditPanel(isOwner(ad))
            mainOnClick(ad)

        }

        private fun getTimeFromMillions(timeMillions: String): String{
            val c = Calendar.getInstance()
            c.timeInMillis = timeMillions.toLong()
            return formatter.format(c.time)
        }

        private fun mainOnClick(ad: Ad) = with(binding) {
            ibEditAd.setOnClickListener(onClickEdit(ad))
            ibDeleteAd.setOnClickListener {
                activity.onDeleteItem(ad)
            }
            ibFav.setOnClickListener {
                if (activity.mAuth.currentUser?.isAnonymous == false) activity.onFavClicked(ad)
            }
            itemView.setOnClickListener {
                activity.onAdViewed(ad)
            }
        }

        private fun isFav(ad: Ad) {
            if (ad.isFav) {
                binding.ibFav.setImageResource(R.drawable.ic_fav_pressed)
            } else {
                binding.ibFav.setImageResource(R.drawable.ic_fav_normal)
            }
        }

        private fun onClickEdit(ad: Ad): View.OnClickListener {
            return View.OnClickListener {
                val editIntent = Intent(activity, EditAdsAct::class.java).apply {
                    putExtra(MainActivity.EDIT_STATE, true)
                    putExtra(MainActivity.ADS_DATA, ad)
                }
                activity.startActivity(editIntent)
            }
        }


        private fun isOwner(ad: Ad): Boolean {
            return activity.mAuth.uid == ad.uid
        }

        private fun showEditPanel(isOwner: Boolean) {
            if (isOwner) {
                binding.editPanel.visibility = View.VISIBLE
            } else {
                binding.editPanel.visibility = View.GONE
            }
        }

    }

    interface Listener {
        fun onDeleteItem(ad: Ad)
        fun onAdViewed(ad: Ad)
        fun onFavClicked(ad: Ad)
    }
}