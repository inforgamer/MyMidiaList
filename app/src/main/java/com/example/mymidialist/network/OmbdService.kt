package com.example.mymidialist.network

import android.R
import androidx.appcompat.widget.DialogTitle
import com.google.android.gms.common.api.internal.ApiKey
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OmbdService{
    @GET("/")
    suspend fun buscarFilmesESeries(
        @Query("apikey") apiKey: String,
        @Query("s") query: String
    ): RespostaOmbd
}

data class RespostaOmbd(
    @SerializedName("Search") val shearch: List<ItemOmdb>?
)

data class ItemOmdb(
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Poster") val poster: String

)
object OmdbInstace{
    val api: OmbdService by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OmbdService::class.java)
    }
}