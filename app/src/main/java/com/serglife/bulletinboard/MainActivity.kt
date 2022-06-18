package com.serglife.bulletinboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.serglife.bulletinboard.databinding.ActivityMainBinding
import com.serglife.bulletinboard.ui.dialog.DialogConst.SING_UP_STATE
import com.serglife.bulletinboard.ui.dialog.DialogHelper

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private val dialogHelper = DialogHelper(this)
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).also { binding = it }.root)
        init()
    }

    private fun init() {
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.mainContent.toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.id_my_ads -> {
                Toast.makeText(this,"Press in my_ads", Toast.LENGTH_LONG).show()
            }
            R.id.id_car -> {
                Toast.makeText(this,"Press in id_car", Toast.LENGTH_LONG).show()
            }
            R.id.id_pc -> {
                Toast.makeText(this,"Press in id_pc", Toast.LENGTH_LONG).show()
            }
            R.id.id_smartphone -> {
                Toast.makeText(this,"Press in id_smartphone", Toast.LENGTH_LONG).show()
            }
            R.id.id_appliances -> {
                Toast.makeText(this,"Press in id_appliances", Toast.LENGTH_LONG).show()
            }
            R.id.id_sing_up -> {
                dialogHelper.createDialog(SING_UP_STATE)
            }
            R.id.id_sing_in -> {
                dialogHelper.createDialog(SING_UP_STATE)
            }
            R.id.id_sing_out -> {
                Toast.makeText(this,"Press in id_sing_out", Toast.LENGTH_LONG).show()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}