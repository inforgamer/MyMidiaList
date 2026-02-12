package com.example.mymidialist.data

import android.util.Log
import com.example.mymidialist.BuildConfig // <--- Importante
import com.example.mymidialist.model.Midia
import com.example.mymidialist.network.GoogleBooksInstance
import com.example.mymidialist.network.IGDBInstance
import com.example.mymidialist.network.RetrofitInstance
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

// MUDAMOS O NOME AQUI PARA "Repository" PARA CORRIGIR O ERRO NA TELA BUSCA
class Repository {

    companion object {
        private const val CLIENT_ID = BuildConfig.IGDB_ID
        private const val CLIENT_SECRET = BuildConfig.IGDB_SECRET

        private var tokenAtual: String = ""
    }

    suspend fun buscarAnimesNaInternet(nomeDigitado: String, tipoDeBusca: String): List<Midia> {

        if (tipoDeBusca == "Mangas") {
            return try {
                val respostaJikan = RetrofitInstance.api.buscarMangas(nomeDigitado)
                respostaJikan.data.filter { manga ->
                    val generos = manga.genres?.map { it.name } ?: emptyList()
                    !generos.contains("Hentai") && !generos.contains("Erotica") && manga.type != "Doujinshi"
                }.map { mangaDetalhes ->
                    Midia(
                        titulo = mangaDetalhes.titulo,
                        tipo = "Mangás",
                        status = if (mangaDetalhes.status == "Finished") "Concluído" else "Lançando",
                        nota = mangaDetalhes.nota?.toInt() ?: 0,
                        comentario = "",
                        imageUrl = mangaDetalhes.imagens.jpg.imageUrl
                    )
                }
            } catch (e: Exception) {
                Log.e("ErroAPI", "Erro ao buscar Mangás: ${e.message}")
                emptyList()
            }

        } else if (tipoDeBusca == "Jogos") {
            return try {
                if (tokenAtual.isEmpty()) {
                    val respToken = IGDBInstance.api.obterToken(CLIENT_ID, CLIENT_SECRET)
                    tokenAtual = "Bearer ${respToken.accessToken}"
                }

                val queryText = "search \"$nomeDigitado\"; fields name, cover.url, total_rating, first_release_date; limit 20;"
                val body = queryText.toRequestBody("text/plain".toMediaTypeOrNull())

                val games = IGDBInstance.api.buscarGames(CLIENT_ID, tokenAtual, body)

                games.map { game ->
                    val capaHd = game.cover?.url?.replace("//", "https://")
                        ?.replace("t_thumb", "t_cover_big")
                        ?: ""

                    Midia(
                        titulo = game.name,
                        tipo = "Jogos",
                        status = "Jogando",
                        nota = game.total_rating?.toInt() ?: 0,
                        comentario = "",
                        imageUrl = capaHd
                    )
                }
            } catch (e: Exception) {
                if (e.message?.contains("401") == true) tokenAtual = ""
                Log.e("IGDB_ERRO", "Erro na busca: ${e.message}")
                emptyList()
            }

        } else if (tipoDeBusca == "Livros") {
            return try {
                val buscaFormatada = nomeDigitado.trim().replace(" ", "+")
                val respostaGoogle = GoogleBooksInstance.api.buscarLivrosGerais(buscaFormatada)

                respostaGoogle.items?.map { livro ->
                    val urlCapa = livro.volumeInfo.imageLinks?.thumbnail?.replace("http:", "https:") ?: ""
                    Midia(
                        titulo = livro.volumeInfo.title,
                        tipo = "Livros",
                        status = "Lendo",
                        nota = 0,
                        comentario = "",
                        imageUrl = urlCapa
                    )
                } ?: emptyList()
            } catch (e: Exception) {
                Log.e("ErroAPI", "Erro no Google: ${e.message}")
                emptyList()
            }

        } else {
            return try {
                val respostaJikan = RetrofitInstance.api.buscarAnimes(nomeDigitado)
                respostaJikan.data.map { animeDetalhes ->
                    Midia(
                        titulo = animeDetalhes.titulo,
                        tipo = "Séries",
                        status = if (animeDetalhes.status == "Finished Airing") "Concluído" else "Lançando",
                        nota = animeDetalhes.nota?.toInt() ?: 0,
                        comentario = "",
                        imageUrl = animeDetalhes.imagens.jpg.imageUrl
                    )
                }
            } catch (e: Exception) {
                Log.e("ErroAPI", "Erro ao buscar Series: ${e.message}")
                emptyList()
            }
        }
    }
}