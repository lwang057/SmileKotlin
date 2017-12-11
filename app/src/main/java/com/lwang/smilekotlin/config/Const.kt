package com.lwang.smilekotlin.config

/**
 * Created by lwang on 17-12-11.
 */
class Const {

    companion object {

        private val appid = "45578"
        private val secret = "4e6e7a13e16a42059a75a9a8931a779f"
        private val auth = "&showapi_sign=$secret&showapi_appid=$appid"


        fun buildUrl(url: String): String {
            return "$url&$auth"
        }

    }
}