package com.eltex.androidschool.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes


fun Context.toast(@StringRes res: Int, short: Boolean = true) {
    val length = if (short) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
    Toast.makeText(this, res, length).show()
}
