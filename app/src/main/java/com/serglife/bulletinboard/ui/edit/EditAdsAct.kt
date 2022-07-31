package com.serglife.bulletinboard.ui.edit

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.databinding.ActivityEditAdsBinding
import com.serglife.bulletinboard.fragment.common.FragmentCloseInterface
import com.serglife.bulletinboard.fragment.ImageListFragment
import com.serglife.bulletinboard.fragment.adapters.ImageAdapter
import com.serglife.bulletinboard.ui.dialogs.country.DialogSpinnerHelper
import com.serglife.bulletinboard.utils.CityHelper
import com.serglife.bulletinboard.utils.ImageManager
import com.serglife.bulletinboard.utils.ImagePiker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.ArrayList

class EditAdsAct : AppCompatActivity(), FragmentCloseInterface {

    var chooseImageFragment: ImageListFragment? = null
    lateinit var binding: ActivityEditAdsBinding
    private lateinit var dialog: DialogSpinnerHelper
    lateinit var imageAdapter: ImageAdapter
    var job: Job? = null
    var editImagePos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityEditAdsBinding.inflate(layoutInflater).also { binding = it }.root)
        init()
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
    }

    private fun init() {
        dialog = DialogSpinnerHelper()
        imageAdapter = ImageAdapter()
        binding.vpImages.adapter = imageAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ImagePiker.showSelectedImages(resultCode, requestCode, data, this)
    }

    fun openChooseImageFragment(valueReturn: List<String>?) {
        chooseImageFragment = ImageListFragment(this, valueReturn)
        binding.scrollViewMain.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .replace(R.id.place_holder, chooseImageFragment!!)
            .commit()
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
                    ImagePiker.getImages(this, 3, ImagePiker.REQUEST_CODE_GET_IMAGES)
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

    fun onClickGetImages(view: View) {
        if (imageAdapter.list.size == 0) {
            ImagePiker.getImages(this, ImagePiker.MAX_IMAGE_COUNT, ImagePiker.REQUEST_CODE_GET_IMAGES)
        } else {
            openChooseImageFragment(null)
            chooseImageFragment?.updateAdapterFromEdit(imageAdapter.list)
        }
    }

    // Realize interface
    override fun onClose(list: List<Bitmap>) {
        binding.scrollViewMain.visibility = View.VISIBLE
        imageAdapter.updateAdapter(list)
        chooseImageFragment = null
    }
}