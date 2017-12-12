package com.lwang.smilekotlin.utils

import com.lwang.smilekotlin.App
import okhttp3.*
import okio.Okio
import java.io.File
import java.io.IOException

/**
 * Created by lwang on 17-12-12.
 */

object ProgressDownload {

    private var progressListener: ProgressListener? = null


    private val mClient: OkHttpClient by lazy {
        OkHttpClient.Builder().addNetworkInterceptor(ProgressInterceptor(listener)).build()
    }


    private val listener: ProgressListener = object : ProgressListener {
        override fun onProgress(readByte: Long, totalByte: Long, done: Boolean) {
            if (progressListener != null) {
                progressListener!!.onProgress(readByte, totalByte, done)
            }
        }

        override fun onSave(filePath: String) {}
    }

    fun downloadPhoto(url: String, progressListener: ProgressListener) {
        val existFilePath: String? = exist(url)
        if (existFilePath != null) {
            progressListener.onSave(existFilePath)
            return
        }
        this.progressListener = progressListener
        val request = Request.Builder()
                .url(url)
                .build()
        mClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val file = File(App.instance.cacheDir,MD5Util.getHashKey(url))
                val sink = Okio.buffer(Okio.sink(file))
                val source = response.body()!!.source()
                sink.writeAll(source)
                sink.flush()
                progressListener.onSave(file.absolutePath)
            }
        })
    }

    fun exist(url: String): String? {
        val file = File(App.instance.cacheDir, MD5Util.getHashKey(url))
        return if (file.exists()) file.absolutePath else null
    }

}
