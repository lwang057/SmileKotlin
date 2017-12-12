package com.lwang.smilekotlin.utils

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*
import java.io.IOException

/**
 * Created by lwang on 17-12-12.
 */


internal class ProgressResponseBody(private val responseBody: ResponseBody, private val progressListener: ProgressListener?) : ResponseBody() {
    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            internal var totalBytesRead: Long = 0

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                progressListener?.onProgress(totalBytesRead, responseBody.contentLength(), bytesRead == -1L)
                return bytesRead
            }
        }
    }
}