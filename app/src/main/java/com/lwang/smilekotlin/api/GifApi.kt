package com.lwang.smilekotlin.api

import com.google.gson.Gson
import com.lwang.smilekotlin.bean.Gif
import com.lwang.smilekotlin.bean.GifResult
import com.lwang.smilekotlin.config.Const.Companion.buildUrl
import java.net.URL

/**
 * Created by lwang on 17-12-12.
 */
class GifApi {

    companion object {

        val baseUrl = "http://route.showapi.com/341-3"

        fun buildBaseUrl(page: Int, maxResult: Int): String {
            return buildUrl("$baseUrl?page=$page&maxResult=$maxResult")
        }

        fun getData(page: Int, maxResult: Int = 5): List<Gif>? {

            var forecastJsonStr: String? = null
            try {
                forecastJsonStr = URL(buildBaseUrl(page, maxResult)).readText()
            } catch (e: Exception) {
                return null
            }
            val data = Gson().fromJson(forecastJsonStr, GifResult::class.java)
            val gifs: List<Gif> = data.showapi_res_body.contentlist
            return if (gifs.isNotEmpty()) gifs else null
        }
    }


}