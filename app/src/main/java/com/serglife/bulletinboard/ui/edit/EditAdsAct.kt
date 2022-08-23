package com.serglife.bulletinboard.ui.edit

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.fxn.utility.PermUtil
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.model.Ad
import com.serglife.bulletinboard.model.DbManager
import com.serglife.bulletinboard.databinding.ActivityEditAdsBinding
import com.serglife.bulletinboard.fragment.common.FragmentCloseInterface
import com.serglife.bulletinboard.fragment.ImageListFragment
import com.serglife.bulletinboard.fragment.adapters.ImageAdapter
import com.serglife.bulletinboard.ui.dialogs.country.DialogSpinnerHelper
import com.serglife.bulletinboard.utils.CityHelper
import com.serglife.bulletinboard.utils.ImagePiker
import kotlinx.coroutines.Job

class EditAdsAct : AppCompatActivity(), FragmentCloseInterface {

    var chooseImageFragment: ImageListFragment? = null
    lateinit var binding: ActivityEditAdsBinding
    private lateinit var dialog: DialogSpinnerHelper
    lateinit var imageAdapter: ImageAdapter
    private val dbManager = DbManager()
    var job: Job? = null
    var editImagePos = 0
    var launcherMultiSelectImage: ActivityResultLauncher<Intent>? = null
    var launcherSingleSelectImage: ActivityResultLauncher<Intent>? = null

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
        launcherMultiSelectImage = ImagePiker.getLauncherForMultiSelectImages(this)
        launcherSingleSelectImage = ImagePiker.getLauncherForSingleImage(this)
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
                    ImagePiker.launcher(this, launcherMultiSelectImage)
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

    fun onClickSelectedCat(view: View) {
            val listCategory = resources.getStringArray(R.array.category).toList()
            dialog.showSpinnerDialog(this, listCategory, binding.tvCat)
    }

    fun onClickGetImages(view: View) {
        if (imageAdapter.list.size == 0) {
            ImagePiker.launcher(this, launcherMultiSelectImage)
        } else {
            openChooseImageFragment(null)
            chooseImageFragment?.updateAdapterFromEdit(imageAdapter.list)
        }
    }

    fun onClickPublish(view: View){

        dbManager.publishAd(fillAd())

    }

    fun fillAd(): Ad{
        val ad: Ad
        binding.apply {
            ad = Ad(
                country = tvCountry.text.toString(),
                city = tvCity.text.toString(),
                tel = edTel.text.toString(),
                index = edIndex.text.toString(),
                withSend = checkBoxWithSend.isChecked.toString(),
                category = tvCat.text.toString(),
                title = edTitleCard.text.toString(),
                price = edPrice.text.toString(),
                description = edDescription.text.toString(),
                key = dbManager.db.push().key,
                uid = dbManager.auth.uid
            )
        }
        return ad
    }

    // Realize interface
    override fun onClose(list: List<Bitmap>) {
        binding.scrollViewMain.visibility = View.VISIBLE
        imageAdapter.updateAdapter(list)
        chooseImageFragment = null
    }
}