package com.serglife.bulletinboard.utils

import android.net.Uri
import android.view.View
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.ui.edit.EditAdsAct
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


object ImagePiker {
    const val MAX_IMAGE_COUNT = 5
    private fun getOptions(imageCounter: Int): Options {
        return Options().apply {
            count = imageCounter
            isFrontFacing = false
            mode = Mode.Picture
            ratio
            path = "/pix/images"
        }
    }

    fun getMultiImages(edAct: EditAdsAct, imageCounter: Int = MAX_IMAGE_COUNT) {
        edAct.addPixToActivity(R.id.place_holder, getOptions(imageCounter)) { result ->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    getMultiSelectImages(edAct, result.data)
                }
                PixEventCallback.Status.BACK_PRESSED -> {
                }
            }
        }
    }

    fun addImages(edAct: EditAdsAct, imageCounter: Int = MAX_IMAGE_COUNT) {

        edAct.addPixToActivity(R.id.place_holder, getOptions(imageCounter)) { result ->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    openChooseImageFragment(edAct)
                    edAct.chooseImageFragment?.updateAdapter(result.data as MutableList<Uri>, edAct)

                }
                PixEventCallback.Status.BACK_PRESSED -> {
                }
            }
        }
    }

    fun getSingleImage(edAct: EditAdsAct) {

        edAct.addPixToActivity(R.id.place_holder, getOptions(1)) { result ->
            when (result.status) {
                PixEventCallback.Status.SUCCESS -> {
                    openChooseImageFragment(edAct)
                    singleImage(edAct, result.data[0])
                }
                PixEventCallback.Status.BACK_PRESSED -> {
                }
            }
        }
    }

    private fun openChooseImageFragment(edAct: EditAdsAct){
        edAct.supportFragmentManager.beginTransaction().replace(R.id.place_holder, edAct.chooseImageFragment!!).commit()
    }

    private fun closePixFragment(edAct: EditAdsAct) {
        val fList = edAct.supportFragmentManager.fragments
        fList.forEach { item ->
            if (item.isVisible) {
                edAct.supportFragmentManager.beginTransaction().remove(item).commit()
            }
        }
    }

    fun getMultiSelectImages(edAct: EditAdsAct, uris: List<Uri>) {

        if (uris.size > 1 && edAct.chooseImageFragment == null) {
            edAct.openChooseImageFragment(uris)
        } else if (uris.size == 1 && edAct.chooseImageFragment == null) {

            edAct.job = CoroutineScope(Dispatchers.Main).launch {
                edAct.binding.pBarLoad.visibility = View.VISIBLE
                val bitmapList = ImageManager.imageResize(uris, edAct)
                edAct.binding.pBarLoad.visibility = View.GONE
                edAct.imageAdapter.updateAdapter(bitmapList)
                closePixFragment(edAct)
            }
        }
    }


    private fun singleImage(edAct: EditAdsAct, uri: Uri) {
        edAct.chooseImageFragment?.setSingleImage(uri, edAct.editImagePos)

    }
}