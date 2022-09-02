package com.serglife.bulletinboard.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serglife.bulletinboard.model.Ad
import com.serglife.bulletinboard.model.DbManager

class FirebaseViewModel : ViewModel() {
    private val dbManager = DbManager()
    val liveAdsData = MutableLiveData<List<Ad>>()

    fun loadAllAd(){
        dbManager.getAllAds(object : DbManager.ReadDataCallback{
            override fun redData(list: List<Ad>) {
                liveAdsData.value = list
            }
        })
    }

    fun loadMyAd(){
        dbManager.getMyAds(object : DbManager.ReadDataCallback{
            override fun redData(list: List<Ad>) {
                liveAdsData.value = list
            }
        })
    }

}