package com.lwang.smilekotlin.bean

import com.google.gson.JsonObject

/**
 * Created by lwang on 17-12-11.
 */
data class Huaban(val title: String,
                  val thumb: String,
                  val url: String)


data class HuabanResult(val showapi_res_code: String,
                        val showapi_res_error: String,
                        val showapi_res_body: JsonObject)