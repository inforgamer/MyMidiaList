package com.example.mymidialist.data

import android.util.Log
import com.example.mymidialist.model.Midia
import com.example.mymidialist.network.RetrofitInstance

class AnimeRepository {
    suspend fun buscarAnimesNaInternet(nomeDigitado: String, tipoDeBusca: String): List<Midia> {
        return try {
            val resposta = if (tipoDeBusca == "Livros"){
                RetrofitInstance.api.buscarMangas(nomeDigitado)
            } else{
                RetrofitInstance.api.buscarAnimes(nomeDigitado)
            }

            resposta.data.map { animeDetalhes ->
                Midia(
                    titulo = animeDetalhes.titulo,
                tipo = if(tipoDeBusca == "Animes") "Series/Filmes" else "Livros",
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