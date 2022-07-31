package com.serglife.bulletinboard.utils

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.serglife.bulletinboard.ui.edit.EditAdsAct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


object ImagePiker {
    const val MAX_IMAGE_COUNT = 5
    const val REQUEST_CODE_GET_IMAGES = 999
    const val REQUEST_CODE_GET_SINGLE_IMAGE = 998
    fun getImages(context: AppCompatActivity, imageCounter: Int, rCode: Int) {
        val options = Options.init()
            .setRequestCode(rCode)
            .setCount(imageCounter)
            .setFrontfacing(false)
            .setMode(Options.Mode.Picture)
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)
            .setPath("/pix/images")

        Pix.start(context, options)
    }

    fun showSelectedImages(resultCode: Int, requestCode: Int, data: Intent?, edAct: EditAdsAct) {
        if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE_GET_IMAGES) {
            if (data != null) {
                val valueReturn = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                if (valueReturn?.size!! > 1 && edAct.chooseImageFragment == null) {
                    edAct.openChooseImageFragment(valueReturn)
                } else if (valueReturn.size == 1 && edAct.chooseImageFragment == null) {

                    edAct.job = CoroutineScope(Dispatchers.Main).launch {
                        edAct.binding.pBarLoad.visibility = View.VISIBLE
                        val bitmapList = ImageManager.imageResize(valueReturn)
                        edAct.binding.pBarLoad.visibility = View.GONE
                        edAct.imageAdapter.updateAdapter(bitmapList)
                    }

                } else if (edAct.chooseImageFragment != null) {
                    edAct.chooseImageFragment?.updateAdapter(valueReturn)
                }
            }
        } else if (resultCode == AppCompatActivity.RESULT_OK && requestCode == REQUEST_CODE_GET_SINGLE_IMAGE) {
            if (data != null) {
                val uris = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                edAct.chooseImageFragment?.setSingleImage(uris?.get(0)!!, edAct.editImagePos)
            }
        }
    }
}