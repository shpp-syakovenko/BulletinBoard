package com.serglife.bulletinboard.fragment

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.get
import androidx.core.view.size
import androidx.recyclerview.widget.ItemTouchHelper
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.databinding.ListImageFragmentBinding
import com.serglife.bulletinboard.fragment.adapters.SelectImageRVAdapter
import com.serglife.bulletinboard.fragment.common.FragmentCloseInterface
import com.serglife.bulletinboard.ui.dialogs.dialog.ProgressDialog
import com.serglife.bulletinboard.ui.edit.EditAdsAct
import com.serglife.bulletinboard.utils.AdapterCallback
import com.serglife.bulletinboard.utils.ImageManager
import com.serglife.bulletinboard.utils.ImagePiker
import com.serglife.bulletinboard.utils.ItemTouchMoveCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ImageListFragment(
    val onFragmentCloseInterface: FragmentCloseInterface
) : BaseAdsFragment(), AdapterCallback {

    private lateinit var binding: ListImageFragmentBinding

    val adapter = SelectImageRVAdapter()
    private val dragCallback = ItemTouchMoveCallback(adapter)
    val touchHelper = ItemTouchHelper(dragCallback)
    private var job: Job? = null
    private var addImageItem: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ListImageFragmentBinding.inflate(layoutInflater).also {
            binding = it
            adView = binding.adView
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolBar()
        init()

        touchHelper.attachToRecyclerView(binding.rvSelectImage)
    }

    private fun init() {
        adapter.adapterCallback = this
        binding.rvSelectImage.adapter = adapter
    }


    override fun onItemDelete() {
        addImageItem?.isVisible = true
    }

    fun updateAdapterFromEdit(bitmapList: List<Bitmap>) {
        adapter.updateAdapter(bitmapList, true)
        if (adapter.list.size >= ImagePiker.MAX_IMAGE_COUNT) addImageItem?.isVisible = false
    }

    private fun setUpToolBar() {
        binding.tbListImage.inflateMenu(R.menu.menu_choose_image)
        val deleteImageItem = binding.tbListImage.menu.findItem(R.id.id_delete_image)
        addImageItem = binding.tbListImage.menu.findItem(R.id.id_add_image)
        if (adapter.list.size >= ImagePiker.MAX_IMAGE_COUNT) addImageItem?.isVisible = false

        binding.tbListImage.setNavigationOnClickListener {
            showInterAd()
        }

        deleteImageItem.setOnMenuItemClickListener {
            adapter.updateAdapter(listOf(), true)
            addImageItem?.isVisible = true
            true
        }

        addImageItem?.setOnMenuItemClickListener {
            val imageCount = ImagePiker.MAX_IMAGE_COUNT - adapter.list.size
            ImagePiker.addImages(
                activity as EditAdsAct,
                imageCount
            )
            true
        }
    }

    fun updateAdapter(newList: MutableList<Uri>, activity: Activity) {
        resizeSelectedImages(newList, false, activity)
    }

    fun setSingleImage(uri: Uri, position: Int) {

        val size = binding.rvSelectImage.size
        Log.d("MyLog","Size: $size Position:$position")

        val pBar = binding.rvSelectImage[position].findViewById<ProgressBar>(R.id.pBar)

        job = CoroutineScope(Dispatchers.Main).launch {
            pBar.visibility = View.VISIBLE
            val bitmapList = ImageManager.imageResize(listOf(uri), activity as Activity)
            pBar.visibility = View.GONE
            adapter.list[position] = bitmapList[0]
            adapter.notifyItemChanged(position)
        }

    }

    fun resizeSelectedImages(newList: List<Uri>, needClear: Boolean, activity: Activity) {
        job = CoroutineScope(Dispatchers.Main).launch {
            val dialog = ProgressDialog.createProgressDialog(activity)
            val bitmapList = ImageManager.imageResize(newList, activity)
            dialog.dismiss()
            adapter.updateAdapter(bitmapList, needClear)
            if (adapter.list.size >= ImagePiker.MAX_IMAGE_COUNT) addImageItem?.isVisible = false
        }
    }

    override fun onClose() {
        super.onClose()
        activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        onFragmentCloseInterface.onClose(adapter.list)
        job?.cancel()
    }


}