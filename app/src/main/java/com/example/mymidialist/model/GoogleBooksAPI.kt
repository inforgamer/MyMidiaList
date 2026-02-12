package com.example.mymidialist.model

import com.google.gson.annotations.SerializedName

data class RespostaGoogleBooks(
    @SerializedName("items") val items: List<ItemLivro>?
)
data class ItemLivro(
    @SerializedName("volumeInfo") val volumeInfo: VolumeInfo
)

data class  VolumeInfo(
    @SerializedName("title") val title: String,
    @SerializedName("authors") val authors: List<String>?,
    @SerializedName("imageLinks") val imageLinks: ImageLinks?
)

data class ImageLinks(
    @SerializedName("thumbnail") val thumbnail: String
)
