/**************************************************************************************************
 * Copyright Finn (c) 2020.                                                                       *
 **************************************************************************************************/

package com.finn.android.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.finn.android.data.TextBean
import kotlinx.android.synthetic.main.rv_test_item.view.*

class TextViewHolder(view: View): RecyclerView.ViewHolder(view){

    open fun bind(
        testText: TextBean,
        onItemClicked: ((site: String) -> Unit)?
    ) {
        with(itemView) {
            item_text.text = testText.desc
        }

        itemView.setOnClickListener {
//            onItemClicked?.invoke(siteText)
        }
    }
}