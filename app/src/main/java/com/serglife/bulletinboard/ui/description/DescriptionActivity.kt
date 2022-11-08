package com.serglife.bulletinboard.ui.description

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.net.toUri
import androidx.viewpager2.widget.ViewPager2
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.databinding.ActivityDescriptionBinding
import com.serglife.bulletinboard.fragment.adapters.ImageAdapter
import com.serglife.bulletinboard.model.Ad
import com.serglife.bulletinboard.utils.ImageManager

class DescriptionActivity : AppCompatActivity() {
    lateinit var binding: ActivityDescriptionBinding
    lateinit var adapter: ImageAdapter
    private var ad: Ad? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            ActivityDescriptionBinding.inflate(layoutInflater).also { binding = it }.root
        )
        init()
        binding.fbTel.setOnClickListener { call() }
        binding.fbEmail.setOnClickListener { sendEmail() }
    }

    private fun init() {
        adapter = ImageAdapter()
        binding.viewPager.adapter = adapter
        getIntentFromMainActivity()
        imageChangeCounter()
    }

    private fun getIntentFromMainActivity() {
        ad = intent.getSerializableExtra(AD) as Ad
        if (ad != null) updateUI(ad!!)
    }

    private fun updateUI(ad: Ad) {
        ImageManager.fillImageArray(ad, adapter)
        fillTextViews(ad)
    }

    private fun fillTextViews(ad: Ad) = with(binding) {
        tvTitleDes.text = ad.title
        tvDesc.text = ad.description
        tvPrice.text = ad.price
        tvTel.text = ad.tel
        tvEmail.text = ad.email
        tvCountry.text = ad.country
        tvCity.text = ad.city
        tvIndex.text = ad.index
        tvWithSend.text = isWithSend(ad.withSend.toBoolean())
        updateImageCounter(0)
    }

    private fun isWithSend(withSend: Boolean): String {
        return if (withSend) getString(R.string.with_send_yes)
        else resources.getString(R.string.with_send_no)
    }

    private fun call() {
        val callUri = "tel:${ad?.tel}"
        val intentCall = Intent(Intent.ACTION_DIAL)
        intentCall.data = callUri.toUri()
        startActivity(intentCall)
    }

    private fun sendEmail(){
        val intentEmail = Intent(Intent.ACTION_SEND)
        intentEmail.type = "message/rfc822"
        intentEmail.apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(ad?.email))
            putExtra(Intent.EXTRA_SUBJECT, "Обьявление")
            putExtra(Intent.EXTRA_TEXT, "Меня интересует ваше обьявление.")
        }

        try {
            startActivity(Intent.createChooser(intentEmail, "Открыть с помощю"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this,
                "У вас нету приложения для отправки сообщения на почту.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun imageChangeCounter(){
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateImageCounter(position)
            }
        })
    }

    private fun updateImageCounter(counter: Int){
        var index = 1
        val itemCount = binding.viewPager.adapter?.itemCount
        if(itemCount == 0) index = 0
        val imageCounter = "${counter + index}/$itemCount"
        binding.tvImageCounter.text = imageCounter
    }

    companion object {
        const val AD = "AD"
    }
}