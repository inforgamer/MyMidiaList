package com.example.mymidialist.model

import com.google.gson.annotations.SerializedName


data class TwitchToken(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("expires_in") val expiresIn: Long,
    @SerializedName("token_type") val tokenType: String? = null
)

data class GameIGDB(
    val id: Long,
    val name: String,
    val cover: GameCover?,
    val first_release_date: Long?,
    val total_rating: Double?
)

data class GameCover(
    val url: String
)