package com.serglife.bulletinboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.serglife.bulletinboard.databinding.ActivityMainBinding
import com.serglife.bulletinboard.fragment.adapters.AdsRVAdapter
import com.serglife.bulletinboard.model.Ad
import com.serglife.bulletinboard.ui.description.DescriptionActivity
import com.serglife.bulletinboard.ui.dialogs.account.AccountHelper
import com.serglife.bulletinboard.ui.dialogs.account.DialogConst.SING_IN_STATE
import com.serglife.bulletinboard.ui.dialogs.account.DialogConst.SING_UP_STATE
import com.serglife.bulletinboard.ui.dialogs.account.DialogHelper
import com.serglife.bulletinboard.ui.edit.EditAdsAct
import com.serglife.bulletinboard.ui.filter.FilterActivity
import com.serglife.bulletinboard.utils.FilterManager
import com.serglife.bulletinboard.viewmodel.FirebaseViewModel
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AdsRVAdapter.Listener {
    private lateinit var tvAccount: TextView
    private lateinit var imAccount: ImageView
    private lateinit var binding: ActivityMainBinding
    private val dialogHelper = DialogHelper(this)
    val mAuth = Firebase.auth
    private val adapter = AdsRVAdapter(this)
    private val viewModel: FirebaseViewModel by viewModels()
    lateinit var googleSingInLauncher: ActivityResultLauncher<Intent>
    lateinit var filterLauncher: ActivityResultLauncher<Intent>
    private var clearUpdate: Boolean = true
    private var currentCategory: String? = null
    private var filter: String = FilterActivity.EMPTY
    private var filterDb: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).also { binding = it }.root)
        init()
        initViewModel()
        initRecyclerView()
        bottomMenuOnClick()
        scrollListener()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.id_filter){
            val intentFilter = Intent(this@MainActivity, FilterActivity::class.java).apply {
                putExtra(FilterActivity.FILTER_KEY, filter)
            }
            filterLauncher.launch(intentFilter)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        binding.mainContent.bNavView.selectedItemId = R.id.id_home
    }

    private fun onActivityResult() {
        googleSingInLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    if (account != null) {
                        dialogHelper.accHelper.singInFireBaseWithGoogle(account.idToken!!)
                    }
                } catch (e: ApiException) {
                    Log.d("MyLog", "Api exception: ${e.message}")
                }
            }
    }

    private fun onActivityResultFilter(){
        filterLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK){
                filter = it.data?.getStringExtra(FilterActivity.FILTER_KEY)!!
                //Log.d("MyLog","filter: $filter")
                //Log.d("MyLog","getFilter: ${FilterManager.getFilter(filter)}")
                filterDb = FilterManager.getFilter(filter)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        uiUpdate(mAuth.currentUser)
    }

    private fun initViewModel() {
        viewModel.liveAdsData.observe(this) { list ->
            if(!clearUpdate){
                adapter.updateAdapter(getAdsByCategory(list))
            }else{
                adapter.updateAdapterWithClear(getAdsByCategory(list))
            }

            binding.mainContent.tvEmpty.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
        }
    }

    private fun getAdsByCategory(list: MutableList<Ad>): MutableList<Ad>{
        val tempList = mutableListOf<Ad>()
        tempList.addAll(list)
        if(currentCategory != getString(R.string.def)){
            tempList.clear()
            list.forEach {
                if(currentCategory == it.category) tempList.add(it)
            }
        }
        tempList.reverse()
        return tempList
    }

    private fun init() {
        currentCategory = getString(R.string.def)
        onActivityResult()
        onActivityResultFilter()
        navViewSettings()
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
        imAccount = binding.navView.getHeaderView(0).findViewById(R.id.imAccountImage)
    }

    private fun initRecyclerView() {
        binding.mainContent.rcView.adapter = adapter
    }

    private fun bottomMenuOnClick() = with(binding) {
        mainContent.bNavView.setOnNavigationItemSelectedListener { item ->
            clearUpdate = true
            when (item.itemId) {
                R.id.id_new_ad -> {
                    val intent = Intent(this@MainActivity, EditAdsAct::class.java)
                    startActivity(intent)
                }
                R.id.id_my_ads -> {
                    viewModel.loadMyAd()
                    mainContent.toolbar.title = getString(R.string.ad_my_ads)
                }
                R.id.id_favs -> {
                    viewModel.loadMyFavs()
                    mainContent.toolbar.title = getString(R.string.ad_my_favs)
                }
                R.id.id_home -> {
                    currentCategory = getString(R.string.def)
                    //Log.d("MyLog","filterDb: $filterDb")
                    viewModel.loadAllAdFirstPage(filterDb)
                    mainContent.toolbar.title = getString(R.string.def)
                }

            }

            true
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        clearUpdate = true
        when (item.itemId) {
            R.id.id_my_ads -> {
                viewModel.loadMyAd()
                binding.mainContent.toolbar.title = getString(R.string.ad_my_ads)
            }
            R.id.id_car -> {
                getAdsFromCat(getString(R.string.ad_car))
            }
            R.id.id_pc -> {
                getAdsFromCat(getString(R.string.ad_pc))
            }
            R.id.id_smartphone -> {
                getAdsFromCat(getString(R.string.ad_smartphone))
            }
            R.id.id_appliances -> {
                getAdsFromCat(getString(R.string.ad_appliances))
            }
            R.id.id_sing_up -> {
                dialogHelper.createDialog(SING_UP_STATE)
            }
            R.id.id_sing_in -> {
                dialogHelper.createDialog(SING_IN_STATE)
            }
            R.id.id_sing_out -> {
                if(mAuth.currentUser?.isAnonymous == true){
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    return true
                }
                uiUpdate(null)
                mAuth.signOut()
                dialogHelper.accHelper.singOutGoogle()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun getAdsFromCat(cat: String){
        currentCategory = cat
        viewModel.loadAllAdFromCat(cat)
        binding.mainContent.toolbar.title = cat
    }

    fun uiUpdate(user: FirebaseUser?) {
         if (user == null) {
           dialogHelper.accHelper.singAnonymously(object : AccountHelper.Listener{
               override fun onComplete() {
                   tvAccount.text = resources.getString(R.string.guest)
                   imAccount.setImageResource(R.drawable.ic_account_def)
               }

           })
        } else if(user.isAnonymous) {
             tvAccount.text = resources.getString(R.string.guest)
             imAccount.setImageResource(R.drawable.ic_account_def)
        }else if(!user.isAnonymous){
            tvAccount.text = user.email
            Picasso.get().load(user.photoUrl).into(imAccount)
         }
    }

    override fun onDeleteItem(ad: Ad) {
        viewModel.deleteItem(ad)
    }

    override fun onAdViewed(ad: Ad) {
        viewModel.adViewed(ad)
        Intent(this, DescriptionActivity::class.java)
            .apply { putExtra(DescriptionActivity.AD, ad) }
            .also { startActivity(it) }
    }

    override fun onFavClicked(ad: Ad) {
        viewModel.onFavClick(ad)
    }

    private fun navViewSettings() = with(binding){
        val menu = navView.menu
        val adsCat = menu.findItem(R.id.adsCat)
        val spanAdsCat = SpannableString(adsCat.title)
        spanAdsCat.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this@MainActivity,R.color.red)),
            0,
            adsCat.title.length,
            0
        )
        adsCat.title = spanAdsCat

        val accCat = menu.findItem(R.id.accCat)
        val spanAccCat = SpannableString(accCat.title)
        spanAccCat.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this@MainActivity,R.color.red)),
            0,
            accCat.title.length,
            0
        )
        accCat.title = spanAccCat
    }

    private fun scrollListener() = with(binding.mainContent) {
        rcView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(SCROLL_DIRECTION_BOTTOM)
                    && newState == RecyclerView.SCROLL_STATE_IDLE
                ) {
                    clearUpdate = false
                    val adsList = viewModel.liveAdsData.value!!
                    if(adsList.isNotEmpty()) {
                        getAdsFromCat(adsList)
                    }
                }
            }
        })
    }

    private fun getAdsFromCat(adsList: MutableList<Ad>) {
        adsList[0].let {
            if (currentCategory == getString(R.string.def)) {
                viewModel.loadAllAdNextPage(it.time)
            } else {
                val catTime = "${it.category}_${it.time}"
                viewModel.loadAllAdFromCatNextPage(catTime)
            }
        }
    }

    companion object{
        const val EDIT_STATE = "edit_state"
        const val ADS_DATA = "ads_data"
        const val SCROLL_DIRECTION_BOTTOM = 1
    }
}