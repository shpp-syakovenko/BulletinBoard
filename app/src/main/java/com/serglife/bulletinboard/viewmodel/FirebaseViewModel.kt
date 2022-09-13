package com.serglife.bulletinboard.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serglife.bulletinboard.model.Ad
import com.serglife.bulletinboard.model.DbManager

class FirebaseViewModel : ViewModel() {
    private val dbManager = DbManager()
    val liveAdsData = MutableLiveData<MutableList<Ad>>()

    fun loadAllAd(){
        dbManager.getAllAds(object : DbManager.ReadDataCallback{
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

    fun adViewed(ad: Ad){
        dbManager.addViewed(ad)
    }

    fun deleteItem(ad: Ad){
        dbManager.deleteAd(ad, object : DbManager.FinishWorkListener{
            override fun onFinish() {
                //loadAllAd()
                val updateList = liveAdsData.value
                updateList?.remove(ad)
                liveAdsData.postValue(updateList)
            }
        })
    }

}