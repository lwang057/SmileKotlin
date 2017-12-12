package com.lwang.smilekotlin.api

import com.google.gson.Gson
import com.lwang.smilekotlin.bean.Rhesis
import com.lwang.smilekotlin.bean.RhesisResult
import com.lwang.smilekotlin.config.Const.Companion.buildUrl
import java.lang.Exception
import java.net.URL

/**
 * Created by lwang on 17-12-11.
 */
class RhesisApi {

    companion object {

        val baseUrl = "http://route.showapi.com/1211-1"

        fun buildBaseUrl(count: Int): String {
            return buildUrl("${baseUrl}?count=$count")
        }

        fun getData(count: Int = 10): List<Rhesis>? {

            var forecastJsonStr: String? = null
            try {
                forecastJsonStr = URL(buildBaseUrl(count)).readText()
            } catch (e: Exception) {
                return null
            }

            val data = Gson().fromJson(forecastJsonStr, RhesisResult::class.java)
            val texts: List<Rhesis> = data.showapi_res_body.data
            return if (texts.isNotEmpty()) texts else null
        }

    }

}