package com.lwang.smilekotlin.service

import com.lwang.smilekotlin.config.Const.Companion.buildUrl

/**
 * Created by lwang on 17-12-11.
 */
class HuabanService {

    companion object {

        val baseUrl = "http://route.showapi.com/819-1"

        fun buildBaseUrl(type: Int, page: Int, num: Int): String {
            return buildUrl("$baseUrl?type=$type&num=$num&page=$page")
        }


//         fun getData(type: Int, page: Int, num: Int = 20): MutableList<Huaban>?{
//
//             var forecastJsonStr: String? = null
//
//             try {
//                 forecastJsonStr = URL(buildBaseUrl(type, page, num)).readText()
//             }catch (e: Exception){
//                 return null
//             }
//
//
//         }
    }


}