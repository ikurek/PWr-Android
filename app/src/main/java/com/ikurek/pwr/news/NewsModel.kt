package com.ikurek.pwr.news

import java.util.*

/**
 * Created by igor on 15.10.17.
 */
data class NewsModel(
        val title: String,
        val url: String,
        val date: Date,
        val source: String)