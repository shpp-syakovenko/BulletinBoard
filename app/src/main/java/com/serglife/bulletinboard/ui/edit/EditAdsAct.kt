package com.serglife.bulletinboard.ui.edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.serglife.bulletinboard.R
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
        dialog.showSpinnerDialog(this, listCountry, binding.tvCountry)
        if(binding.tvCity.text.toString() != getString(R.string.selected_city)){
            binding.tvCity.text = getString(R.string.selected_city)
        }
    }

    fun onClickSelectedCities(view:View){

        val selectedCountry = binding.tvCountry.text.toString()

        if(selectedCountry != getString(R.string.selected_country)) {
            val listCities = CityHelper.getAllCities(this, selectedCountry)
            dialog.showSpinnerDialog(this, listCities, binding.tvCity)
        }else{
            Toast.makeText(this, getString(R.string.choose_country_first), Toast.LENGTH_SHORT).show()
        }
    }
}