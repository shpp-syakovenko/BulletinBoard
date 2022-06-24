package com.serglife.bulletinboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.serglife.bulletinboard.databinding.ActivityMainBinding
import com.serglife.bulletinboard.ui.dialog.DialogConst.SING_IN_STATE
import com.serglife.bulletinboard.ui.dialog.DialogConst.SING_UP_STATE
import com.serglife.bulletinboard.ui.dialog.DialogHelper
import com.serglife.bulletinboard.ui.dialog.GoogleConst
import com.serglife.bulletinboard.ui.edit.EditAdsAct

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var tvAccount: TextView
    private lateinit var binding: ActivityMainBinding
    private val dialogHelper = DialogHelper(this)
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).also { binding = it }.root)
        init()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.id_new_ads -> {
                val intent = Intent(this, EditAdsAct::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == GoogleConst.GOOGLE_SING_IN_REQUEST_CODE){
           // Log.d("MyLog","on Activity Result!!!!!!!!!!!!")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                if(account != null){
                    dialogHelper.accHelper.singInFireBaseWithGoogle(account.idToken!!)
                }
            }catch (e: ApiException){
                Log.d("MyLog", "Api exception: ${e.message}")
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        super.onStart()
        uiUpdate(mAuth.currentUser)
    }

    private fun init() {
        setSupportActionBar(binding.mainContent.toolbar)
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
        tvAccount = binding.navView.getHeaderView(0).findViewById(R.id.tvAccountEmail)
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
                dialogHelper.createDialog(SING_IN_STATE)
            }
            R.id.id_sing_out -> {
                uiUpdate(null)
                mAuth.signOut()
                dialogHelper.accHelper.singOutGoogle()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun uiUpdate(user: FirebaseUser?){
        tvAccount.text = if (user == null){
            resources.getString(R.string.not_reg)
        }else{
            user.email
        }
    }
}