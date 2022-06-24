package com.serglife.bulletinboard.ui.edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.serglife.bulletinboard.databinding.ActivityEditAdsBinding

class EditAdsAct : AppCompatActivity() {
    private lateinit var binding: ActivityEditAdsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityEditAdsBinding.inflate(layoutInflater).also { binding = it }.root)
    }
}