package com.serglife.bulletinboard.utils

import com.serglife.bulletinboard.model.Ad
import com.serglife.bulletinboard.model.AdFilter
import com.serglife.bulletinboard.ui.filter.FilterActivity

object FilterManager {
    fun createFilter(ad: Ad): AdFilter{
        return AdFilter(
            time = ad.time,
            cat_time = "${ad.category}_${ad.time}",

            cat_country_withSend_time = "${ad.category}_${ad.country}_${ad.withSend}_${ad.time}",
            cat_country_city_withSend_time = "${ad.category}_${ad.country}_${ad.city}_${ad.withSend}_${ad.time}",
            cat_country_city_index_withSend_time = "${ad.category}_${ad.country}_${ad.city}_${ad.index}_${ad.withSend}_${ad.time}",
            cat_index_withSend_time = "${ad.category}_${ad.index}_${ad.withSend}_${ad.time}",
            cat_withSend_time = "${ad.category}_${ad.withSend}_${ad.time}",

            country_withSend_time = "${ad.country}_${ad.withSend}_${ad.time}",
            country_city_withSend_time = "${ad.country}_${ad.city}_${ad.withSend}_${ad.time}",
            country_city_index_withSend_time = "${ad.country}_${ad.city}_${ad.index}_${ad.withSend}_${ad.time}",
            index_withSend_time = "${ad.index}_${ad.withSend}_${ad.time}",
            withSend_time = "${ad.withSend}_${ad.time}"
        )

    }

    fun getFilter(filter: String): String{
        val sBuilderNode = StringBuilder()
        val sBuilderFilter = StringBuilder()
        val tempList = filter.split("_")

        if(tempList[0] != FilterActivity.EMPTY){
            sBuilderNode.append("country_")
            sBuilderFilter.append("${tempList[0]}_")
        }
        if(tempList[1] != FilterActivity.EMPTY){
            sBuilderNode.append("city_")
            sBuilderFilter.append("${tempList[1]}_")
        }
        if(tempList[2] != FilterActivity.EMPTY){
            sBuilderNode.append("index_")
            sBuilderFilter.append("${tempList[2]}_")
        }
        sBuilderNode.append("withSend_time")
        sBuilderFilter.append(tempList[3])

        return "$sBuilderNode|$sBuilderFilter"
    }
}