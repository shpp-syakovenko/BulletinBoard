package com.serglife.bulletinboard.ui.description

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.serglife.bulletinboard.R
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
        updateUI(ad)
    }

    private fun updateUI(ad: Ad){
        fillImageArray(ad)
        fillTextViews(ad)
    }

    private fun fillTextViews(ad: Ad) = with(binding){
        tvTitleDes.text = ad.title
        tvDesc.text = ad.description
        tvPrice.text = ad.price
        tvTel.text = ad.tel
        tvEmail.text = ad.email
        tvCountry.text = ad.country
        tvCity.text = ad.city
        tvIndex.text = ad.index
        tvWithSend.text = isWithSend(ad.withSend.toBoolean())
    }

    private fun isWithSend(withSend: Boolean): String {
        return if (withSend) getString(R.string.with_send_yes)
        else resources.getString(R.string.with_send_no)
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