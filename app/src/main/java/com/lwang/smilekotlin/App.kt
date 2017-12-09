package com.lwang.smilekotlin

import android.app.Application
import com.yhao.commen.notNullSingleValue

/**
 * Created by lwang on 17-12-9.
 */
class App : Application() {

    companion object {
        var instance: App by notNullSingleValue()
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}