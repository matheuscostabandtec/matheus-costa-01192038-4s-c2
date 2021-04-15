package com.example.apicachorros

import retrofit2.Call
import retrofit2.http.*


interface ApiCachorros {

    @GET("cachorros")
    fun getTodos(): Call<List<Cachorros>>

    @GET("cachorros/{id}")
    fun getById(@Path("id")id:Int): Call<Cachorros>
}