package com.serglife.bulletinboard.ui.edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.serglife.bulletinboard.databinding.ActivityEditAdsBinding
import com.serglife.bulletinboard.ui.dialogs.country.DialogSpinnerHelper
import com.serglife.bulletinboard.utils.CityHelper

class EditAdsAct : AppCompatActivity() {

    lateinit var binding: ActivityEditAdsBinding
    private lateinit var dialog: DialogSpinnerHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(ActivityEditAdsBinding.inflate(layoutInflater).also { binding = it }.root)
        init()

    }

    private fun init(){
        dialog = DialogSpinnerHelper()
    }

    //onClicks
    fun onClickSelectedCountry(view:View){
        val listCountry = CityHelper.getAllCountries(this)
        dialog.showSpinnerDialog(this, listCountry)
    }
}