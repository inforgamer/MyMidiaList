package com.example.mymidialist.data

import android.util.Log
import androidx.room.util.query
import com.example.mymidialist.BuildConfig
import com.example.mymidialist.model.Midia
import com.example.mymidialist.network.GoogleBooksInstance
import com.example.mymidialist.network.RawgService
import com.example.mymidialist.network.RetrofitInstance
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody


class Repository {


    suspend fun buscarAnimesNaInternet(nomeDigitado: String, tipoDeBusca: String): List<Midia> {

        // Mangas
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
                        imageUrl = mangaDetalhes.imagens.jpg.imageUrl,
                        description = mangaDetalhes.sinopse?.replace("[Written by MAL Rewrite]", "")?.trim() ?: "Sem sinopse."
                    )
                }
            } catch (e: Exception) {
                Log.e("ErroAPI", "Erro ao buscar Mangás: ${e.message}")
                emptyList()
            }

            // SÉRIES
        } else if (tipoDeBusca == "Séries") {
            return try {
                val respostaJikan = RetrofitInstance.api.buscarAnimes(nomeDigitado)
                respostaJikan.data.filter { anime ->
                    val generos = anime.genres?.map { it.name } ?: emptyList()
                    !generos.contains("Hentai") && !generos.contains("Erotica")
                }.map { animeDetalhes ->
                    Midia(
                        titulo = animeDetalhes.titulo,
                        tipo = "Séries",
                        status = if (animeDetalhes.status == "Finished Airing") "Concluído" else "Lançando",
                        nota = animeDetalhes.nota?.toInt() ?: 0,
                        comentario = "",
                        imageUrl = animeDetalhes.imagens.jpg.imageUrl,
                        description = animeDetalhes.sinopse?.replace("[Written by MAL Rewrite]", "")?.trim() ?: "Sem sinopse."
                    )
                }
            } catch (e: Exception) {
                Log.e("ErroAPI", "Erro ao buscar Animes: ${e.message}")
                emptyList()
            }

            // jogos
        } else if (tipoDeBusca == "Jogos") {
        return try {
            val apiKey = BuildConfig.RAWG_KEY

            val resposta = com.example.mymidialist.network.RawgInstance.api.buscarGames(
                apiKey = apiKey,
                query  = nomeDigitado
            )

            resposta.results.map { game ->
                Midia(
                    titulo = game.name,
                    tipo = "Jogos",
                    status = "",
                    nota = game.metacritic?: 0,
                    comentario = "",
                    imageUrl = game.background_image ?:"",
                    description = "Lançado em: ${game.released ?: "Data desconhecida"}"
                )
            }
        } catch (e: Exception) {
                Log.e("RAWG_ERRO", "Erro na busca: ${e.message}")
                emptyList()
            }

            // livros
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
                        imageUrl = urlCapa,
                        description = livro.volumeInfo.description ?: "Sem descrição."
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
                        imageUrl = animeDetalhes.imagens.jpg.imageUrl,
                        description = animeDetalhes.sinopse?.replace("[Written by MAL Rewrite]", "")?.trim() ?: "Sem sinopse."
                    )
                }
            } catch (e: Exception) {
                Log.e("ErroAPI", "Erro ao buscar Series (Else): ${e.message}")
                emptyList()
            }
        }
    }
}