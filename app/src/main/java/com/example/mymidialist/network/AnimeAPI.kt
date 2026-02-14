package com.example.mymidialist.network

import com.google.gson.annotations.SerializedName

data class RespostaDaApi(
    val data: List<AnimeDetalhes>
)

data class AnimeDetalhes(
    @SerializedName("mal_id") val id: Int,
    @SerializedName("title") val titulo: String,
    @SerializedName("status") val status: String,
    @SerializedName("score") val nota: Double?,
    @SerializedName("images") val imagens: AnimeImagens,
    @SerializedName("genres") val genres: List<Genero>?,
    @SerializedName("type") val type: String?,
    @SerializedName("synopsis") val sinopse: String?
)
data class Genero(
    @SerializedName("name") val name: String
)

data class AnimeImagens(
    @SerializedName("jpg") val jpg: AnimeImageUrl
)

data class AnimeImageUrl(
    @SerializedName("image_url") val imageUrl: String
)
