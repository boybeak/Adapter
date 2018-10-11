package com.github.boybeak.autobind

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("list")
    fun list(@Query("admin_id") admin_id: Int,
             @Query("role") role: String,
             @Query("studio_name") studio_name: String,
             @Query("page") page: Int,
             @Query("limit") limit: Int): Call<Result<List<Crash>>>

    @GET("get-info")
    fun getInfo(@Query("id") id: Int): Call<Result<Crash>>

}