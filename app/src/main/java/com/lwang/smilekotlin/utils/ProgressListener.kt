package com.lwang.smilekotlin.utils

/**
 * Created by lwang on 17-12-12.
 */
interface ProgressListener {
    fun onProgress(readByte: Long, totalByte: Long, done: Boolean)

    fun onSave(filePath: String)
}
