package com.eltex.androidschool.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes


fun Context.toast(@StringRes res: Int, short: Boolean = true) {
    if (short)
        Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
    else
        Toast.makeText(this, res, Toast.LENGTH_LONG).show()
}
