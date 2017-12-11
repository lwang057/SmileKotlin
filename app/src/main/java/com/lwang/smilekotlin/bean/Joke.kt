package com.lwang.smilekotlin.bean

/**
 * Created by lwang on 17-12-9.
 */
data class Joke(val title: String,
                val text: String)


data class JokeResult(val showapi_res_code: String,
                      val showapi_res_error: String,
                      val showapi_res_body: JokeBody)


data class JokeBody(val allNum: String,
                    val allPages: String,
                    val currentPage: String,
                    val maxResult: String,
                    val contentlist: List<Joke>)

