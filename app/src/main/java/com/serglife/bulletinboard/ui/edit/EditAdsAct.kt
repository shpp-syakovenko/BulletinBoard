package com.serglife.bulletinboard.ui.edit

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.databinding.ActivityEditAdsBinding
import com.serglife.bulletinboard.ui.dialogs.country.DialogSpinnerHelper
import com.serglife.bulletinboard.utils.CityHelper
import com.serglife.bulletinboard.utils.ImagePiker

class EditAdsAct : AppCompatActivity() {

    lateinit var binding: ActivityEditAdsBinding
    private lateinit var dialog: DialogSpinnerHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(ActivityEditAdsBinding.inflate(layoutInflater).also { binding = it }.root)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == ImagePiker.REQUEST_CODE_GET_IMAGES){
            if (data != null){
                val valueReturn = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ImagePiker.getImages(this)
                } else {
                    Toast.makeText(
                        this,
                        "Approve permissions to open Pix ImagePiker",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun init() {
        dialog = DialogSpinnerHelper()

    }

    //onClicks
    fun onClickSelectedCountry(view: View) {
        val listCountry = CityHelper.getAllCountries(this)
        dialog.showSpinnerDialog(this, listCountry, binding.tvCountry)
        if (binding.tvCity.text.toString() != getString(R.string.selected_city)) {
            binding.tvCity.text = getString(R.string.selected_city)
        }
    }

    fun onClickSelectedCities(view: View) {

        val selectedCountry = binding.tvCountry.text.toString()

        if (selectedCountry != getString(R.string.selected_country)) {
            val listCities = CityHelper.getAllCities(this, selectedCountry)
            dialog.showSpinnerDialog(this, listCities, binding.tvCity)
        } else {
            Toast.makeText(this, getString(R.string.choose_country_first), Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun onClickGetImages(view:View){
        ImagePiker.getImages(this)
    }
}