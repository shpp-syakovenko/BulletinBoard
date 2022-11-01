package com.serglife.bulletinboard.model

data class AdFilter(
    val time: String? = null,
    val cat_time: String? = null,

    val cat_country_withSend_time: String? = null,
    val cat_country_city_withSend_time: String? = null,
    val cat_country_city_index_withSend_time: String? = null,
    val cat_index_withSend_time: String? = null,
    val cat_withSend_time: String? = null,

    val country_withSend_time: String? = null,
    val country_city_withSend_time: String? = null,
    val country_city_index_withSend_time: String? = null,
    val index_withSend_time: String? = null,
    val withSend_time: String? = null
)
