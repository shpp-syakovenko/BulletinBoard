package com.serglife.bulletinboard.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.databinding.ListImageFragmentBinding
import com.serglife.bulletinboard.fragment.adapters.SelectImageRVAdapter
import com.serglife.bulletinboard.fragment.common.FragmentCloseInterface
import com.serglife.bulletinboard.fragment.common.SelectImageItem

class ImageListFragment(val onFragmentCloseInterface: FragmentCloseInterface, val list: List<String>): Fragment() {
    private lateinit var binding: ListImageFragmentBinding
    val adapter = SelectImageRVAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ListImageFragmentBinding.inflate(layoutInflater).also { binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSelectImage.adapter = adapter

        val updateList = mutableListOf<SelectImageItem>()
        for(i in list.indices){
            updateList.add(SelectImageItem(i.toString(), list[i]))
        }
        adapter.updateAdapter(updateList)
        binding.bBack.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
    }

    override fun onDetach() {
        super.onDetach()
        onFragmentCloseInterface.onClose()
    }
}