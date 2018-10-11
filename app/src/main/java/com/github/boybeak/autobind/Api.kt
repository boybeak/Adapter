package com.github.boybeak.autobind

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object Api {
    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://api.teacher.meishuquanyunxiao.com/v2/error/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        val service = retrofit.create(ApiService::class.java)
    }
}