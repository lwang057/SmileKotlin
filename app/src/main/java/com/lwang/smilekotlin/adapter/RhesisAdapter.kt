package com.lwang.smilekotlin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lwang.smilekotlin.R
import com.lwang.smilekotlin.bean.Rhesis
import org.jetbrains.anko.find

/**
 * Created by lwang on 17-12-11.
 */
class RhesisAdapter(var items: List<Rhesis>?) : RecyclerView.Adapter<RhesisAdapter.MyViewHolder>() {

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.textView.text = items?.get(position)?.english + "\n" + "\n" + items?.get(position)?.chinese
    }

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rhesis, parent, false))
    }


    class MyViewHolder(val item: View) : RecyclerView.ViewHolder(item) {
        val textView: TextView = item.find(R.id.text)
    }

}