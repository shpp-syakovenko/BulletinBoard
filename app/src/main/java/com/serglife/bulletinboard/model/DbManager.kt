package com.serglife.bulletinboard.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class DbManager {
    val db = Firebase.database.getReference(MAIN_NODE)
    val dbStorage = Firebase.storage.getReference(MAIN_NODE)
    val auth = Firebase.auth

    fun publishAd(ad: Ad, finishListener: FinishWorkListener) {
        if (auth.uid != null) {
            db.child(ad.key ?: "empty").child(auth.uid!!).child(AD_NODE).setValue(ad)
                .addOnCompleteListener {
                    finishListener.onFinish()
                }
        }
    }

    fun addViewed(ad: Ad){
        var counter = ad.viewsCounter.toInt()
        counter++
        if(auth.uid != null){
            db.child(ad.key ?: "empty")
                .child(INFO_NODE)
                .setValue(InfoItem(
                    viewsCounter = counter.toString(),
                    emailsCounter = ad.emailsCounter,
                    callsCounter = ad.callsCounter
                ))
        }
    }

    fun onFavClick(ad: Ad, listener: FinishWorkListener){
        if(ad.isFav) removeToFavs(ad, listener)
        else addToFavs(ad,listener)
    }

    private fun addToFavs(ad: Ad, listener: FinishWorkListener) {
        ad.key?.let { key ->
            auth.uid?.let { uid ->
                db.child(key).child(FAVS_NODE).child(uid).setValue(uid).addOnCompleteListener {
                    if(it.isSuccessful) listener.onFinish()
                }
            }
        }
    }

    private fun removeToFavs(ad: Ad, listener: FinishWorkListener) {
        ad.key?.let { key ->
            auth.uid?.let { uid ->
                db.child(key).child(FAVS_NODE).child(uid).removeValue().addOnCompleteListener {
                    if(it.isSuccessful) listener.onFinish()
                }
            }
        }
    }

    fun getMyAds(readDataCallback: ReadDataCallback?){
        val query = db.orderByChild(auth.uid + "/ad/uid").equalTo(auth.uid)
        readDataFromDb(query, readDataCallback)
    }

    fun getMyFavs(readDataCallback: ReadDataCallback?){
        val query = db.orderByChild("/favs/${auth.uid}").equalTo(auth.uid)
        readDataFromDb(query, readDataCallback)
    }

    fun getAllAds(readDataCallback: ReadDataCallback?){
        val query = db.orderByChild(auth.uid + "/ad/price")
        readDataFromDb(query, readDataCallback)
    }

    fun deleteAd(ad: Ad, listener: FinishWorkListener){
        if(ad.key == null || ad.uid == null) return
        db.child(ad.key).child(ad.uid).removeValue().addOnCompleteListener {
            if(it.isSuccessful) listener.onFinish()
        }
    }



    private fun readDataFromDb(query: Query, readDataCallback: ReadDataCallback?) {
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listAd = mutableListOf<Ad>()
                for (item in snapshot.children) {

                    var ad:Ad? = null
                    item.children.forEach {
                        if(ad == null) ad = it.child(AD_NODE).getValue(Ad::class.java)
                    }
                    val infoItem = item.child(INFO_NODE).getValue(InfoItem::class.java)
                    val favsCounter = item.child(FAVS_NODE).childrenCount.toString()
                    val isFavs = auth.uid?.let { item.child(FAVS_NODE).child(it).getValue(String::class.java) }

                    ad?.apply {
                        isFav = isFavs != null
                        viewsCounter = infoItem?.viewsCounter ?: "0"
                        emailsCounter = infoItem?.emailsCounter ?: "0"
                        callsCounter = infoItem?.callsCounter ?: "0"
                        favCounter = favsCounter
                    }
                    if(ad != null) listAd.add(ad!!)
                }
                readDataCallback?.redData(listAd)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
    interface ReadDataCallback {
        fun redData(list: MutableList<Ad>)
    }
    interface FinishWorkListener{
        fun onFinish()
    }

    companion object{
        const val AD_NODE = "ad"
        const val INFO_NODE = "info"
        const val MAIN_NODE = "main"
        const val FAVS_NODE = "favs"
    }
}