package com.serglife.bulletinboard.ui.filter

import android.content.Intent
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
        getFilter()
        onClickSelectedCountry()
        onClickSelectedCities()
        onClickDone()
        onClickClear()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    private fun actionBarSettings() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun getFilter() = with(binding){
        val filter = intent.getStringExtra(FILTER_KEY)
        if(filter != null && filter != EMPTY){
            val filterList = filter.split("_")
            if(filterList[0] != EMPTY) tvCountry.text = filterList[0]
            if(filterList[1] != EMPTY) tvCity.text = filterList[1]
            if(filterList[2] != EMPTY) edIndex.setText(filterList[2])
            checkBoxWithSend.isChecked = filterList[3].toBoolean()
        }
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
            }else{
                sBuilder.append(EMPTY)
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
            val intentFilter = Intent().apply {
                putExtra(FILTER_KEY, createFilter())
            }
            setResult(RESULT_OK, intentFilter)
            finish()
        }
    }

    private fun onClickClear() = with(binding) {
        btClear.setOnClickListener {
            tvCountry.text = getString(R.string.selected_country)
            tvCity.text = getString(R.string.selected_city)
            edIndex.setText("")
            checkBoxWithSend.isChecked = false
            setResult(RESULT_CANCELED)
        }
    }

    companion object{
        const val FILTER_KEY = "filter_key"
        const val EMPTY = "empty"
    }


}