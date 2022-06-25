package com.serglife.bulletinboard.utils

import android.content.Context
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

object CityHelper {
    fun getAllCountries(context: Context): List<String> {
        val list = mutableListOf<String>()
        try {
            val inputStream: InputStream = context.assets.open("countriesToCities.json")
            val size = inputStream.available()
            val bytesArray = ByteArray(size)
            inputStream.read(bytesArray)
            val jsonFile = String(bytesArray)
            val jsonObject = JSONObject(jsonFile)
            val countriesNames = jsonObject.names()

            if (countriesNames != null) {
                for (i in 0 until countriesNames.length()) {
                    list.add(countriesNames.getString(i))
                }
            }

        } catch (e: IOException) {

        }
        return list
    }
}