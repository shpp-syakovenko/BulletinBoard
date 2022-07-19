package com.serglife.bulletinboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.databinding.ListImageFragmentBinding
import com.serglife.bulletinboard.fragment.adapters.SelectImageRVAdapter
import com.serglife.bulletinboard.fragment.common.FragmentCloseInterface
import com.serglife.bulletinboard.utils.ImagePiker
import com.serglife.bulletinboard.utils.ItemTouchMoveCallback

class ImageListFragment(
    val onFragmentCloseInterface: FragmentCloseInterface,
    val list: List<String>
) : Fragment() {

    private lateinit var binding: ListImageFragmentBinding
    val adapter = SelectImageRVAdapter()
    private val dragCallback = ItemTouchMoveCallback(adapter)
    val touchHelper = ItemTouchHelper(dragCallback)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ListImageFragmentBinding.inflate(layoutInflater).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolBar()

        touchHelper.attachToRecyclerView(binding.rvSelectImage)
        binding.rvSelectImage.adapter = adapter
        adapter.updateAdapter(list, true)

    }

    private fun setUpToolBar() {
        binding.tbListImage.inflateMenu(R.menu.menu_choose_image)
        val deleteImageItem = binding.tbListImage.menu.findItem(R.id.id_delete_image)
        val addImageItem = binding.tbListImage.menu.findItem(R.id.id_add_image)

        binding.tbListImage.setNavigationOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

        deleteImageItem.setOnMenuItemClickListener {
            adapter.updateAdapter(listOf(), true)
            true
        }

        addImageItem.setOnMenuItemClickListener {
            val imageCount = ImagePiker.MAX_IMAGE_COUNT - adapter.list.size
            ImagePiker.getImages(activity as AppCompatActivity, imageCount)
            true
        }
    }

    fun updateAdapter(newList: MutableList<String>){
        adapter.updateAdapter(newList, false)
    }

    override fun onDetach() {
        super.onDetach()
        onFragmentCloseInterface.onClose(adapter.list)
    }
}