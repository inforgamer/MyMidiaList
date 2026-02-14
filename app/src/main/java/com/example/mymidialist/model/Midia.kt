package com.example.mymidialist.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tabela_midia")
data class Midia(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val titulo: String,
    val tipo: String,
    val status: String,
    val nota: Int = 0,
    val comentario: String = "",
    val imageUrl: String = "",
    val description: String = ""
)