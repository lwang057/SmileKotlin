package com.lwang.smilekotlin.utils

import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import com.lwang.smilekotlin.R

/**
 * Created by lwang on 17-12-11.
 */
class ToastUtil {

    companion object {
        /**
         * 从下边出来的提示框
         */
        fun showSnackbar(viewGroup: ViewGroup, text: String, duration: Int = 1000) {

            val snack = Snackbar.make(viewGroup, text, duration)
            snack.view.setBackgroundColor(ContextCompat.getColor(viewGroup.context, R.color.colorPrimary))
            snack.show()
        }
    }
}