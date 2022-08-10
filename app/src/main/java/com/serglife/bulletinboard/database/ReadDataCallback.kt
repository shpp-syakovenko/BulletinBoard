package com.serglife.bulletinboard.database

import com.serglife.bulletinboard.data.Ad

interface ReadDataCallback {
    fun redData(list: List<Ad>)
}