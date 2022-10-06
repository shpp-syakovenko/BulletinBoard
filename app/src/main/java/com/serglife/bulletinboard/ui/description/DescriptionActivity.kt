package com.serglife.bulletinboard.ui.description

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.serglife.bulletinboard.databinding.ActivityDescriptionBinding
import com.serglife.bulletinboard.fragment.adapters.ImageAdapter
import com.serglife.bulletinboard.model.Ad
import com.serglife.bulletinboard.utils.ImageManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DescriptionActivity : AppCompatActivity() {
    lateinit var binding: ActivityDescriptionBinding
    lateinit var adapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityDescriptionBinding.inflate(layoutInflater).also { binding = it }.root)
        init()
    }

    private fun init(){
        adapter = ImageAdapter()
        binding.viewPager.adapter = adapter
        getIntentFromMainActivity()
    }

    private fun getIntentFromMainActivity(){
        val ad = intent.getSerializableExtra(AD) as Ad
        fillImageArray(ad)
    }

    private fun fillImageArray(ad: Ad){
        val listUris = listOf(ad.mainImage, ad.image2, ad.image3, ad.image4, ad.image5)
        CoroutineScope(Dispatchers.Main).launch {
            val bitMapList = ImageManager.getBitmapFromUris(listUris)
            adapter.updateAdapter(bitMapList)
        }
    }

    companion object{
        const val AD = "AD"
    }
}