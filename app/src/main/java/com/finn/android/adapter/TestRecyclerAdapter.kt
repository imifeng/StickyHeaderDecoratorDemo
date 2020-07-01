/**************************************************************************************************
 * Copyright Finn (c) 2020.                                                                       *
 **************************************************************************************************/

package com.finn.android.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.finn.android.R
import com.finn.android.data.TextBean
import com.finn.android.extension.inflate
import com.finn.android.holder.TextViewHolder

class TestRecyclerAdapter : RecyclerView.Adapter<TextViewHolder>() {

    private val textBeans: MutableList<TextBean> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        return TextViewHolder(parent.inflate(R.layout.rv_test_item))
    }

    override fun getItemCount()= textBeans.size

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        holder.bind(textBeans[position], null)
    }

    fun addAllItems(items: List<TextBean>) {
        textBeans.clear()
        textBeans.addAll(items)
        notifyDataSetChanged()
    }

    fun removeByPosition(position: Int) {
        textBeans.removeAt(position)
        notifyItemRemoved(position)
    }
}