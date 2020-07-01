/**************************************************************************************************
 * Copyright Finn (c) 2020.                                                                       *
 **************************************************************************************************/

package com.finn.android.extension

import android.view.View

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}