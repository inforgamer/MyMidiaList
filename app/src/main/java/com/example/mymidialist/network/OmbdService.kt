package com.example.mymidialist.network

import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbService {
    @GET("/")
    suspend fun buscarFilmesESeries(
        @Query("apikey") apiKey: String,
        @Query("s") query: String,
        @Query("type") tipo: String
    ): RespostaOmdb
}

data class RespostaOmdb(
    @SerializedName("Search") val search: List<ItemOmdb>?
)

data class ItemOmdb(
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Poster") val poster: String
)

object OmdbInstance {
    val api: OmdbService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OmdbService::class.java)
    }
}