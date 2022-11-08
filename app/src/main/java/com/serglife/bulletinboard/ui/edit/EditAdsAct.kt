package com.serglife.bulletinboard.ui.edit

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.tasks.OnCompleteListener
import com.serglife.bulletinboard.MainActivity
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.model.Ad
import com.serglife.bulletinboard.model.DbManager
import com.serglife.bulletinboard.databinding.ActivityEditAdsBinding
import com.serglife.bulletinboard.ext.makeTransparentStatusBar
import com.serglife.bulletinboard.fragment.common.FragmentCloseInterface
import com.serglife.bulletinboard.fragment.ImageListFragment
import com.serglife.bulletinboard.fragment.adapters.ImageAdapter
import com.serglife.bulletinboard.ui.dialogs.country.DialogSpinnerHelper
import com.serglife.bulletinboard.utils.CityHelper
import com.serglife.bulletinboard.utils.ImageManager
import com.serglife.bulletinboard.utils.ImagePiker
import kotlinx.coroutines.Job
import java.io.ByteArrayOutputStream

class EditAdsAct : AppCompatActivity(), FragmentCloseInterface {

    var chooseImageFragment: ImageListFragment? = null
    lateinit var binding: ActivityEditAdsBinding
    private lateinit var dialog: DialogSpinnerHelper
    lateinit var imageAdapter: ImageAdapter
    private val dbManager = DbManager()
    var job: Job? = null
    var editImagePos = 0
    private var imageIndex = 0
    private var isEditState = false
    private var ad: Ad? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityEditAdsBinding.inflate(layoutInflater).also { binding = it }.root)
        makeTransparentStatusBar()
        init()
        checkEditState()
        imageChangeCounter()
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

    private fun checkEditState(){
        isEditState = isEditState()
        if(isEditState){
            ad = intent.getSerializableExtra(MainActivity.ADS_DATA) as Ad
            if(ad != null) fillViews(ad!!)
        }
    }

    private fun isEditState():Boolean{
        return intent.getBooleanExtra(MainActivity.EDIT_STATE, false)
    }

    private fun fillViews(ad: Ad) = with(binding){
        tvCountry.text = ad.country
        tvCity.text = ad.city
        edTel.setText(ad.tel)
        edIndex.setText(ad.index)
        checkBoxWithSend.isChecked = ad.withSend.toBoolean()
        tvCat.text = ad.category
        edTitleCard.setText(ad.title)
        edPrice.setText(ad.price)
        edDescription.setText(ad.description)
        edEmail.setText(ad.email)
        ImageManager.fillImageArray(ad, imageAdapter)
        updateImageCounter(0)
    }

    fun openChooseImageFragment(newList: List<Uri>?) {
        chooseImageFragment = ImageListFragment(this)
        if(newList != null) chooseImageFragment?.resizeSelectedImages(newList, true, this)
        binding.scrollViewMain.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .replace(R.id.place_holder, chooseImageFragment!!)
            .commit()
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
            ImagePiker.getMultiImages(this)
        } else {
            openChooseImageFragment(null)
            chooseImageFragment?.updateAdapterFromEdit(imageAdapter.list)
        }
    }

    fun onClickPublish(view: View) {
        ad = fillAd()
        uploadImages()
    }

    fun onPublishFinish() = object : DbManager.FinishWorkListener{
        override fun onFinish() {
            finish()
        }
    }

    private fun uploadImages(){
        if(imageIndex == ImagePiker.MAX_IMAGE_COUNT){
            dbManager.publishAd(ad!!, onPublishFinish())
            return
        }
        val oldUrl = getUrlFromAd()

        if(imageAdapter.list.size > imageIndex) {
            val byteArray = prepareImageByteArray(imageAdapter.list[imageIndex])

            if(oldUrl.startsWith("http")){
                updateImage(byteArray, oldUrl){
                    nextImage(it.result.toString())
                }
            }else{
                uploadImage(byteArray) {
                    nextImage(it.result.toString())
                }
            }

        }else{
            if(oldUrl.startsWith("http")){
                deleteImageByUrl(oldUrl){
                    nextImage("empty")
                }
            }else{
                nextImage("empty")
            }
        }
    }

    private fun nextImage(uri: String){
        setImageUriToAd(uri)
        imageIndex++
        uploadImages()
    }

    private fun setImageUriToAd(uri: String){
        when(imageIndex){
            0 -> ad = ad?.copy(mainImage = uri)
            1 -> ad = ad?.copy(image2 = uri)
            2 -> ad = ad?.copy(image3 = uri)
            3 -> ad = ad?.copy(image4 = uri)
            4 -> ad = ad?.copy(image5 = uri)
        }
    }

    private fun getUrlFromAd(): String{
        return listOf(ad?.mainImage!!, ad?.image2!!, ad?.image3!!, ad?.image4!!, ad?.image5!!)[imageIndex]
    }

    private fun prepareImageByteArray(bitmap: Bitmap): ByteArray{
        val outStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outStream)
        return outStream.toByteArray()
    }

    private fun uploadImage(byteArray: ByteArray, listener: OnCompleteListener<Uri>){
        val inStorageRef = dbManager.dbStorage
            .child(dbManager.auth.uid!!)
            .child("image_${System.currentTimeMillis()}")

        val upTask = inStorageRef.putBytes(byteArray)
        upTask.continueWithTask {
            inStorageRef.downloadUrl
        }.addOnCompleteListener(listener)
    }

    private fun deleteImageByUrl(oldUrl: String, listener: OnCompleteListener<Void>){
        dbManager.dbStorage.storage.getReferenceFromUrl(oldUrl)
            .delete().addOnCompleteListener(listener)
    }

    private fun updateImage(byteArray: ByteArray, url: String, listener: OnCompleteListener<Uri>){
        val inStorageRef = dbManager.dbStorage.storage.getReferenceFromUrl(url)
        val upTask = inStorageRef.putBytes(byteArray)
        upTask.continueWithTask {
            inStorageRef.downloadUrl
        }.addOnCompleteListener(listener)
    }

    fun fillAd(): Ad{
        val adTemp: Ad
        binding.apply {
            adTemp = Ad(
                country = tvCountry.text.toString(),
                city = tvCity.text.toString(),
                tel = edTel.text.toString(),
                index = edIndex.text.toString(),
                withSend = checkBoxWithSend.isChecked.toString(),
                category = tvCat.text.toString(),
                title = edTitleCard.text.toString(),
                price = edPrice.text.toString(),
                description = edDescription.text.toString(),
                email = edEmail.text.toString(),
                mainImage = ad?.mainImage ?: "empty",
                image2 = ad?.image2 ?: "empty",
                image3 = ad?.image3 ?: "empty",
                image4 = ad?.image4 ?: "empty",
                image5 = ad?.image5 ?: "empty",
                key = ad?.key ?: dbManager.db.push().key,
                uid = dbManager.auth.uid,
                time = ad?.time ?: System.currentTimeMillis().toString()
            )
        }
        return adTemp
    }

    private fun imageChangeCounter(){
        binding.vpImages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateImageCounter(position)
            }
        })
    }

    private fun updateImageCounter(counter: Int){
        var index = 1
        val itemCount = binding.vpImages.adapter?.itemCount
        if(itemCount == 0) index = 0
        val imageCounter = "${counter + index}/$itemCount"
        binding.tvImageCounter.text = imageCounter
    }

    // Realize interface
    override fun onClose(list: List<Bitmap>) {
        binding.scrollViewMain.visibility = View.VISIBLE
        imageAdapter.updateAdapter(list)
        chooseImageFragment = null
        updateImageCounter(binding.vpImages.currentItem)
    }
}