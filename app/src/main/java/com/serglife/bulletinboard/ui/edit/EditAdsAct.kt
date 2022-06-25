package com.serglife.bulletinboard.ui.edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.serglife.bulletinboard.databinding.ActivityEditAdsBinding
import com.serglife.bulletinboard.utils.CityHelper

class EditAdsAct : AppCompatActivity() {

    private lateinit var binding: ActivityEditAdsBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(ActivityEditAdsBinding.inflate(layoutInflater).also { binding = it }.root)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            CityHelper.getAllCountries(this)
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCountry.adapter = adapter

    }
}