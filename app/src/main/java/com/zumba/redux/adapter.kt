package com.zumba.redux

import ViewItem
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var list: List<ViewItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object: RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)) {

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val textView = holder.itemView.findViewById<TextView>(R.id.text_view)
        textView.text = list[position].name
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun bindList(list: List<ViewItem>) {
        this.list = list
        notifyDataSetChanged()
    }
}