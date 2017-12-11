package com.lwang.smilekotlin.service

import com.google.gson.Gson
import com.lwang.smilekotlin.bean.Huaban
import com.lwang.smilekotlin.bean.HuabanResult
import com.lwang.smilekotlin.config.Const.Companion.buildUrl
import java.net.URL

/**
 * Created by lwang on 17-12-11.
 */
class HuabanService {

    companion object {

        val baseUrl = "http://route.showapi.com/819-1"

        fun buildBaseUrl(type: Int, page: Int, num: Int): String {
            return buildUrl("$baseUrl?type=$type&num=$num&page=$page")
        }


        fun getData(type: Int, page: Int, num: Int = 20): MutableList<Huaban>? {

            var forecastJsonStr: String? = null

            try {
                forecastJsonStr = URL(buildBaseUrl(type, page, num)).readText()
            } catch (e: Exception) {
                return null
            }

            val data = Gson().fromJson(forecastJsonStr, HuabanResult::class.java)
            val iterator = data.showapi_res_body.entrySet().iterator()
            val huabans: MutableList<Huaban> = ArrayList()

            while (iterator.hasNext()) {
                val element = iterator.next()
                try {
                    val huaban = Gson().fromJson(element.value, Huaban::class.java)
                    huabans.add(huaban)
                } catch (e: Exception) {

                }
            }
            return if (huabans.size > 0) huabans else null
        }
    }


}