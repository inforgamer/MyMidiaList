package com.example.mymidialist.network

import com.example.mymidialist.model.RespostaDaApi
import com.example.mymidialist.model.RespostaGoogleBooks
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface AnimeService {
    @GET("v4/anime")
    suspend fun buscarAnimes(
        @Query("q") consulta: String,
        @Query("sfw") sfw: Boolean = true
    ): RespostaDaApi

    @GET("v4/manga")
    suspend fun buscarMangas(
        @Query("q") consulta: String,
        @Query("sfw") sfw: Boolean = true
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

interface GoogleBooksService{
    @GET("books/v1/volumes")
    suspend fun buscarLivrosGerais(
        @Query("q") consulta: String,
        @Query("maxResults") max: Int = 20
    ): RespostaGoogleBooks
}

object GoogleBooksInstance {
    private const val BASE_URL = "https://www.googleapis.com/"

    val api: GoogleBooksService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleBooksService::class.java)
    }
}
