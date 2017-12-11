package com.lwang.smilekotlin.service

import com.google.gson.Gson
import com.lwang.smilekotlin.bean.Joke
import com.lwang.smilekotlin.bean.JokeResult
import com.lwang.smilekotlin.config.Const.Companion.buildUrl
import java.lang.Exception
import java.net.URL

/**
 * Created by lwang on 17-12-11.
 */
class JokeService {

    companion object {

        val baseUrl = "http://route.showapi.com/341-1"

        fun buildBaseUrl(page: Int, maxResult: Int): String {
            return buildUrl("$baseUrl?page=$page&maxResult=$maxResult")
        }

        fun getData(page: Int, maxResult: Int = 10): List<Joke>? {

            var forecastJsonStr: String? = null
            try {
                forecastJsonStr = URL(buildBaseUrl(page, maxResult)).readText()
            } catch (e: Exception) {
                return null
            }

            val data = Gson().fromJson(forecastJsonStr, JokeResult::class.java)
            val jokes: List<Joke> = data.showapi_res_body.contentlist
            return if (jokes.isNotEmpty()) jokes else null
        }

    }

}