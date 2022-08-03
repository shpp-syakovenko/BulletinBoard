package com.serglife.bulletinboard.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.fragment.common.InterAdsClose


open class BaseAdsFragment: Fragment(), InterAdsClose {

    lateinit var adView: AdView
    private var interAd : InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadInterAd()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAds()
    }

    override fun onResume() {
        super.onResume()
        adView.resume()
    }

    override fun onPause() {
        super.onPause()
        adView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        adView.destroy()
    }

    private fun initAds(){
        MobileAds.initialize(activity as Activity)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    private fun loadInterAd(){
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context as Activity, getString(R.string.ad_inter_id), adRequest, object : InterstitialAdLoadCallback(){
            override fun onAdLoaded(ad: InterstitialAd) {
                interAd = ad
            }
        })
    }

    fun showInterAd(){
        if (interAd != null){
            interAd?.fullScreenContentCallback = object : FullScreenContentCallback(){
                override fun onAdDismissedFullScreenContent() {
                    onClose()
                }

                override fun onAdFailedToShowFullScreenContent(e: AdError) {
                    onClose()
                }
            }
            interAd?.show(activity as Activity)
        }else{
            onClose()
        }
    }

    override fun onClose() {}
}