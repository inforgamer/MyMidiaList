package com.example.mymidialist.network

import com.example.mymidialist.model.GameIGDB
import com.example.mymidialist.model.TwitchToken
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface IGDBService {


    @POST("https://id.twitch.tv/oauth2/token")
    suspend fun obterToken(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("grant_type") grantType: String = "client_credentials"
    ): TwitchToken

    @POST("https://api.igdb.com/v4/games")
    suspend fun buscarGames(
        @Header("Client-ID") clientId: String,
        @Header("Authorization") accessToken: String,
        @Body query: RequestBody
    ): List<GameIGDB>
}

object IGDBInstance {
    val api: IGDBService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.igdb.com/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IGDBService::class.java)
    }
}