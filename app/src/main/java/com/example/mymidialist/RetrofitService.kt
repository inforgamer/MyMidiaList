package com.example.mymidialist.network

import com.example.mymidialist.model.RespostaDaApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query


interface AnimeService {
    @GET("v4/anime")
    suspend fun buscarAnimes(
        @Query("q") consulta: String
    ): RespostaDaApi
}

object RetrofitInstance {
    private const val BASE_URL = "https://api.jikan.moe/"

    val api: AnimeService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnimeService::class.java)
    }
}
