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
        val dialog = AlertDialog.Builder(context).create()
        val rootView = LayoutInflater.from(context).inflate(R.layout.spinner_layout, null)
        val adapter = RvDialogSpinnerAdapter(context, dialog)
        val rvCountry = rootView.findViewById<RecyclerView>(R.id.rvSpinnerView)
        val sv = rootView.findViewById<SearchView>(R.id.svSpinner)
        rvCountry.adapter = adapter
        dialog.setView(rootView)
        adapter.updateAdapter(list)
        setSearchView(adapter, list, sv)
        dialog.show()

    }

    private fun setSearchView(adapter: RvDialogSpinnerAdapter, list: List<String>, sv: SearchView?) {
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