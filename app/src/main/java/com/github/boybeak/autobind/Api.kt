package com.github.boybeak.autobind

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


object Api {
    val service: ApiService
    init {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
                .baseUrl("http://api.teacher.meishuquanyunxiao.com/v2/error/")
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        service = retrofit.create(ApiService::class.java)
    }
}