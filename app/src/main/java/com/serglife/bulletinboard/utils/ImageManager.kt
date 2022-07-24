package com.serglife.bulletinboard.utils

import android.graphics.BitmapFactory
import android.util.Log
import android.widget.HeterogeneousExpandableList
import androidx.exifinterface.media.ExifInterface

import java.io.File

object ImageManager {

    const val MAX_IMAGE_SIZE = 1000
    const val WIDTH = 0
    const val HEIGHT = 1

    fun getImageSize(uri: String): List<Int> {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(uri, options)
        return if (imageRotation(uri) == 90) listOf(options.outHeight, options.outWidth)
        else listOf(options.outWidth, options.outHeight)
    }

    private fun imageRotation(uri: String): Int {
        val rotation : Int
        val imageFile = File(uri)
        val exif = ExifInterface(imageFile.absolutePath)
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        rotation =
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90 || orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                90
            } else {
                0
            }

        return rotation
    }

    fun imageResize(uris: List<String>){
        val tempList = mutableListOf<List<Int>>()

        for (i in uris.indices){
            val size = getImageSize(uris[i])
            Log.d("MyLog","WIDTH: ${size[WIDTH]} HEIGHT: ${size[HEIGHT]}")
            val imageRatio = size[WIDTH].toFloat() / size[HEIGHT].toFloat()

            if(imageRatio > 1){ // width > height
                if(size[WIDTH] > MAX_IMAGE_SIZE){
                    tempList.add(listOf(MAX_IMAGE_SIZE,(MAX_IMAGE_SIZE / imageRatio).toInt()))
                }else{
                    tempList.add(listOf(size[WIDTH], size[HEIGHT]))
                }
            }else{ // height > width || ==
                if(size[HEIGHT] > MAX_IMAGE_SIZE){
                    tempList.add(listOf((MAX_IMAGE_SIZE * imageRatio).toInt(), MAX_IMAGE_SIZE))
                }else{
                    tempList.add(listOf(size[WIDTH], size[HEIGHT]))
                }
            }
            Log.d("MyLog","WIDTH: ${tempList[i][WIDTH]} HEIGHT: ${tempList[i][HEIGHT]}")

        }
    }
}