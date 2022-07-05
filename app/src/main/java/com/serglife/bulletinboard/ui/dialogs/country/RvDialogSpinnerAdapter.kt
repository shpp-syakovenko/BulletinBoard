package com.serglife.bulletinboard.ui.dialogs.country

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.serglife.bulletinboard.R
import com.serglife.bulletinboard.ui.edit.EditAdsAct

class RvDialogSpinnerAdapter(val context: Context, val dialog: AlertDialog): RecyclerView.Adapter<RvDialogSpinnerAdapter.SpViewHolder>() {

    val list = mutableListOf<String>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SpViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sp_list_item, parent, false)
        return SpViewHolder(view, context, dialog)

    }

    override fun onBindViewHolder(
        holder: SpViewHolder,
        position: Int
    ) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateAdapter(list: List<String>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class SpViewHolder(itemView: View, val context: Context,val dialog: AlertDialog) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var currentTextCountry = ""

        fun setData(text: String){
            val tvSpItem = itemView.findViewById<TextView>(R.id.tvSpItem)
            tvSpItem.text = text
            currentTextCountry = text
            tvSpItem.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            (context as EditAdsAct).binding.tvCountry.text = currentTextCountry
            dialog.dismiss()
        }

    }
}

