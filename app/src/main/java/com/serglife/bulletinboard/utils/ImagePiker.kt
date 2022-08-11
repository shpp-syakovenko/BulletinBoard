package com.serglife.bulletinboard.utils

import android.content.Intent
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.PermUtil
import com.serglife.bulletinboard.ui.edit.EditAdsAct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


object ImagePiker {
    const val MAX_IMAGE_COUNT = 5
    private fun getOptions(imageCounter: Int): Options {
        return Options.init()
            .setCount(imageCounter)
            .setFrontfacing(false)
            .setMode(Options.Mode.Picture)
            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)
            .setPath("/pix/images")
    }

    fun launcher(
        edAct: EditAdsAct,
        launcher: ActivityResultLauncher<Intent>?,
        imageCounter: Int = MAX_IMAGE_COUNT
    ) {
        PermUtil.checkForCamaraWritePermissions(edAct) {
            val intent = Intent(edAct, Pix::class.java).apply {
                putExtra("options", getOptions(imageCounter))
            }
            launcher?.launch(intent)
        }
    }

    fun getLauncherForMultiSelectImages(edAct: EditAdsAct): ActivityResultLauncher<Intent> {
        return edAct.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                if (result.data != null) {
                    val valueReturn = result.data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
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
            }
        }
    }

    fun getLauncherForSingleImage(edAct: EditAdsAct): ActivityResultLauncher<Intent> {
        return edAct.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                if (result.data != null) {
                    val uris = result.data?.getStringArrayListExtra(Pix.IMAGE_RESULTS)
                    edAct.chooseImageFragment?.setSingleImage(uris?.get(0)!!, edAct.editImagePos)
                }
            }
        }

    }
}