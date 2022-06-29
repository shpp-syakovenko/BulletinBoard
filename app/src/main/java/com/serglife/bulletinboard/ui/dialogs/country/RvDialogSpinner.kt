package com.serglife.bulletinboard.ui.dialogs.country

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.serglife.bulletinboard.R

class RvDialogSpinner: RecyclerView.Adapter<RvDialogSpinner.SpViewHolder>() {

    val list = mutableListOf<String>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SpViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sp_list_item, parent, false)
        return SpViewHolder(view)

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

    class SpViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun setData(text: String){
            val tvSpItem = itemView.findViewById<TextView>(R.id.tvSpItem)
            tvSpItem.text = text
        }

    }
}

