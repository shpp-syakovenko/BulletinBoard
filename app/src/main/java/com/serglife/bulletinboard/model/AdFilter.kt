package com.serglife.bulletinboard.model

data class AdFilter(
    val time: String? = null,
    val cat_time: String? = null,

    val cat_country_withSent_time: String? = null,
    val cat_country_city_withSent_time: String? = null,
    val cat_country_city_index_withSent_time: String? = null,
    val cat_index_withSent_time: String? = null,
    val cat_withSent_time: String? = null,

    val country_withSent_time: String? = null,
    val country_city_withSent_time: String? = null,
    val country_city_index_withSent_time: String? = null,
    val index_withSent_time: String? = null,
    val withSent_time: String? = null
)
