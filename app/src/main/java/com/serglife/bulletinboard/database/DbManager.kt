package com.serglife.bulletinboard.database

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.serglife.bulletinboard.data.Ad

class DbManager {
    val db = Firebase.database.getReference("main")
    val auth = Firebase.auth

    fun publishAd(ad: Ad){
        if(auth.uid != null) {
            db.child(ad.key ?: "empty").child(auth.uid!!).child("ad").setValue(ad)
        }
    }
}