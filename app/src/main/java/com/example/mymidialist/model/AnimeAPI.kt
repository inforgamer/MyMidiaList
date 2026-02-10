package com.example.mymidialist.model

import com.google.gson.annotations.SerializedName

data class RespostaDaApi(
    val data: List<AnimeDetalhes>
)

data class AnimeDetalhes(
    @SerializedName("mal_id") val id: Int,
    @SerializedName("title") val titulo: String,
    @SerializedName("status") val status: String,
    @SerializedName("score") val nota: Double?,
    @SerializedName("images") val imagens: AnimeImagens
)

data class AnimeImagens(
    @SerializedName("jpg") val jpg: AnimeImageUrl
)

data class AnimeImageUrl(
    @SerializedName("image_url") val imageUrl: String
)