package com.example.mymidialist.network

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
    @SerializedName("imageLinks") val imageLinks: ImageLinks?,
    @SerializedName("description") val description: String?

)

data class ImageLinks(
    @SerializedName("thumbnail") val thumbnail: String
)
