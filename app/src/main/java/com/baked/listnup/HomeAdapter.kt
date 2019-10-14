package com.baked.listnup

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.home_list_item.view.*
import kotlinx.android.synthetic.main.home_list_item.*

class HomeAdapter(val items : ArrayList<String>, val context: Context, val clickListener: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Gets the number of titles in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).clickableView?.text = items[holder.adapterPosition]
        holder.clickableView.setOnClickListener {
            clickListener(position)
        }
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView
    val clickableView = view.list_title
}