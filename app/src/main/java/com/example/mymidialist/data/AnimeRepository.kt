package com.example.mymidialist.data

import android.util.Log
import com.example.mymidialist.model.Midia
import com.example.mymidialist.network.RetrofitInstance

class AnimeRepository {
    suspend fun buscarAnimesNaInternet(nomeDigitado: String): List<Midia> {
        return try {
            val resposta = RetrofitInstance.api.buscarAnimes(nomeDigitado)

            resposta.data.map { animeDetalhes ->
                Midia(
                    titulo = animeDetalhes.titulo,
                    tipo = "Animes",
                    status = if (animeDetalhes.status == "Finished Airing") "Concluído" else "Lançando",
                    nota = animeDetalhes.nota?.toInt() ?: 0,
                    comentario = "",
                    imageUrl = animeDetalhes.imagens.jpg.imageUrl
                )
            }
        } catch (e: Exception) {
            Log.e("ErroAPI", "Deu ruim: ${e.message}")
            emptyList()
        }
    }
}