package com.lwang.smilekotlin

import android.app.Application

/**
 * Created by lwang on 17-12-12.
 */

class App : Application() {

    companion object {

        var instance: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


}
