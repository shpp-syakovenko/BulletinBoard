package com.serglife.bulletinboard.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serglife.bulletinboard.model.Ad
import com.serglife.bulletinboard.model.DbManager

class FirebaseViewModel : ViewModel() {
    private val dbManager = DbManager()
    val liveAdsData = MutableLiveData<MutableList<Ad>>()

    fun loadAllAdFirstPage(filter: String){
        dbManager.getAllAdsFirstPage(filter, object : DbManager.ReadDataCallback{
            override fun redData(list: MutableList<Ad>) {
                liveAdsData.value = list
            }
        })
    }

    fun loadAllAdNextPage(time: String){
        dbManager.getAllAdsNextPage(time, object : DbManager.ReadDataCallback{
            override fun redData(list: MutableList<Ad>) {
                liveAdsData.value = list
            }
        })
    }

    fun loadAllAdFromCat(cat: String){
        dbManager.getAllAdsFromCatFirstPage(cat, object : DbManager.ReadDataCallback{
            override fun redData(list: MutableList<Ad>) {
                liveAdsData.value = list
            }
        })
    }

    fun loadAllAdFromCatNextPage(catTime: String){
        dbManager.getAllAdsFromCatNextPage(catTime, object : DbManager.ReadDataCallback{
            override fun redData(list: MutableList<Ad>) {
                liveAdsData.value = list
            }
        })
    }

    fun loadMyAd(){
        dbManager.getMyAds(object : DbManager.ReadDataCallback{
            override fun redData(list: MutableList<Ad>) {
                liveAdsData.value = list
            }
        })
    }

    fun loadMyFavs(){
        dbManager.getMyFavs(object : DbManager.ReadDataCallback{
            override fun redData(list: MutableList<Ad>) {
                liveAdsData.value = list
            }
        })
    }

    fun adViewed(ad: Ad){
        dbManager.addViewed(ad)
    }

    fun onFavClick(ad: Ad){
        dbManager.onFavClick(ad, object : DbManager.FinishWorkListener {
            override fun onFinish() {
                val updateList = liveAdsData.value
                val pos = updateList?.indexOf(ad)
                if(pos != -1){
                    pos?.let {
                        val favCounter = if(ad.isFav) ad.favCounter.toInt() - 1 else ad.favCounter.toInt() + 1
                        updateList[pos] = updateList[pos].copy(isFav = !ad.isFav, favCounter = favCounter.toString())
                    }
                }
                liveAdsData.postValue(updateList)
            }
        })
    }

    fun deleteItem(ad: Ad){
        dbManager.deleteAd(ad, object : DbManager.FinishWorkListener{
            override fun onFinish() {
                val updateList = liveAdsData.value
                updateList?.remove(ad)
                liveAdsData.postValue(updateList)
            }
        })
    }
}