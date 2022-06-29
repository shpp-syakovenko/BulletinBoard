package com.serglife.bulletinboard.ui.edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.serglife.bulletinboard.databinding.ActivityEditAdsBinding
import com.serglife.bulletinboard.ui.dialogs.country.DialogSpinnerHelper
import com.serglife.bulletinboard.utils.CityHelper

class EditAdsAct : AppCompatActivity() {

    private lateinit var binding: ActivityEditAdsBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(ActivityEditAdsBinding.inflate(layoutInflater).also { binding = it }.root)

        val listCountry = CityHelper.getAllCountries(this)
        val dialog = DialogSpinnerHelper()
        dialog.showSpinnerDialog(this, listCountry)



    }
}