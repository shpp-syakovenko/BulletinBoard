package com.serglife.bulletinboard.ui.filter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.databinding.ActivityFilterBinding
import com.serglife.bulletinboard.ui.dialogs.country.DialogSpinnerHelper
import com.serglife.bulletinboard.utils.CityHelper

class FilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFilterBinding
    private val dialog = DialogSpinnerHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityFilterBinding.inflate(layoutInflater).also { binding = it }.root)
        actionBarSettings()
        onClickSelectedCountry()
        onClickSelectedCities()
        onClickDone()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    private fun actionBarSettings() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun createFilter(): String = with(binding) {
        val sBuilder = StringBuilder()
        val arrayTempFilter = listOf(
            tvCountry.text.toString(),
            tvCity.text.toString(),
            edIndex.text.toString(),
            checkBoxWithSend.isChecked.toString()
        )

        for ((index, value) in arrayTempFilter.withIndex()) {
            if (value != getString(R.string.selected_country)
                && value != getString(R.string.selected_city)
                && value.isNotEmpty()
            ) {
                sBuilder.append(value)
                if (index != arrayTempFilter.size - 1) sBuilder.append("_")
            }
        }
        return sBuilder.toString()
    }

    //onClicks
    private fun onClickSelectedCountry() = with(binding) {
        tvCountry.setOnClickListener{
            val listCountry = CityHelper.getAllCountries(this@FilterActivity)
            dialog.showSpinnerDialog(this@FilterActivity, listCountry, tvCountry)
            if (tvCity.text.toString() != getString(R.string.selected_city)) {
                tvCity.text = getString(R.string.selected_city)
            }
        }
    }

    private fun onClickSelectedCities() = with(binding) {
        tvCity.setOnClickListener {
            val selectedCountry = tvCountry.text.toString()
            if (selectedCountry != getString(R.string.selected_country)) {
                val listCities = CityHelper.getAllCities(this@FilterActivity, selectedCountry)
                dialog.showSpinnerDialog(this@FilterActivity, listCities, binding.tvCity)
            } else {
                Toast.makeText(this@FilterActivity, getString(R.string.choose_country_first), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun onClickDone() = with(binding) {
        btDone.setOnClickListener {
            Log.d("MyLog", "string: ${createFilter()}")
        }
    }


}