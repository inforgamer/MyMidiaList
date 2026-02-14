package com.example.mymidialist.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RawgService{
    @GET("games")
    suspend fun buscarGames(
        @Query("key") apiKey: String,
        @Query("search") query: String,
        @Query("page_size") pageSize: Int =20
    ): RespostaRawg
}

data class RespostaRawg(val results: List<GameRawg>)

data class GameRawg(
    val id: Int,
    val name: String,
    val background_image: String?,
    val metacritic: Int?,
    val released: String?

)
object RawgInstance {
    val api: RawgService by lazy{
        Retrofit.Builder()
            .baseUrl("https://api.rawg.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RawgService::class.java)
    }
}