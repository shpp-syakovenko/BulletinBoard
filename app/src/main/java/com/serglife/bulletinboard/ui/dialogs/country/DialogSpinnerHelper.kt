package com.serglife.bulletinboard.ui.dialogs.country

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.utils.CityHelper

class DialogSpinnerHelper {

    fun showSpinnerDialog(context: Context, list: List<String>){
        val builder = AlertDialog.Builder(context)
        val rootView = LayoutInflater.from(context).inflate(R.layout.spinner_layout, null)
        val adapter = RvDialogSpinner()
        val rvCountry = rootView.findViewById<RecyclerView>(R.id.rvSpinnerView)
        val sv = rootView.findViewById<SearchView>(R.id.svSpinner)
        rvCountry.adapter = adapter
        builder.setView(rootView)
        adapter.updateAdapter(list)
        setSearchView(adapter, list, sv)
        builder.show()

    }

    private fun setSearchView(adapter: RvDialogSpinner, list: List<String>, sv: SearchView?) {
        sv?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.updateAdapter(CityHelper.filerListData(list, newText))
                return true
            }

        })
    }
}