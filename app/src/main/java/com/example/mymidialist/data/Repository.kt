package com.example.mymidialist.data

import android.util.Log
import com.example.mymidialist.BuildConfig
import com.example.mymidialist.model.Midia
import com.example.mymidialist.network.GoogleBooksInstance
import com.example.mymidialist.network.OmdbInstance
import com.example.mymidialist.network.RetrofitInstance

class Repository {

    suspend fun buscarAnimesNaInternet(nomeDigitado: String, tipoDeBusca: String): List<Midia> {

        return when (tipoDeBusca) {
            "Mangas" -> try {
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

            "Animes" -> try {
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

            "Séries" -> try {
                val respostaOmdb = OmdbInstance.api.buscarFilmesESeries(
                    apiKey = BuildConfig.OMDB_KEY,
                    query = nomeDigitado,
                    tipo = "series"
                )
                respostaOmdb.search?.map { item ->
                    Midia(
                        titulo = item.title,
                        tipo = "Séries",
                        status = "Planejando",
                        nota = 0,
                        comentario = "",
                        imageUrl = if (item.poster == "N/A") "" else item.poster,
                        description = "Série lançada em: ${item.year}"
                    )
                } ?: emptyList()
            } catch (e: Exception) {
                Log.e("ErroAPI", "Erro ao buscar séries OMDb: ${e.message}")
                emptyList()
            }

            "Jogos" -> try {
                val resposta = com.example.mymidialist.network.RawgInstance.api.buscarGames(
                    apiKey = BuildConfig.RAWG_KEY,
                    query = nomeDigitado
                )
                resposta.results.map { game ->
                    Midia(
                        titulo = game.name,
                        tipo = "Jogos",
                        status = "Jogando",
                        nota = game.metacritic ?: 0,
                        comentario = "",
                        imageUrl = game.background_image ?: "",
                        description = "Lançado em: ${game.released ?: "Data desconhecida"}"
                    )
                }
            } catch (e: Exception) {
                Log.e("RAWG_ERRO", "Erro na busca: ${e.message}")
                emptyList()
            }

            "Livros" -> try {
                val buscaFormatada = nomeDigitado.trim().replace(" ", "+")
                val respostaGoogle = GoogleBooksInstance.api.buscarLivrosGerais(buscaFormatada)
                respostaGoogle.items?.map { livro ->
                    Midia(
                        titulo = livro.volumeInfo.title,
                        tipo = "Livros",
                        status = "Lendo",
                        nota = 0,
                        comentario = "",
                        imageUrl = livro.volumeInfo.imageLinks?.thumbnail?.replace("http:", "https:") ?: "",
                        description = livro.volumeInfo.description ?: "Sem descrição."
                    )
                } ?: emptyList()
            } catch (e: Exception) {
                Log.e("ErroAPI", "Erro no Google: ${e.message}")
                emptyList()
            }

            "Filmes" -> try {
                val resposta = OmdbInstance.api.buscarFilmesESeries(
                    apiKey = BuildConfig.OMDB_KEY,
                    query = nomeDigitado,
                    tipo = "movie"
                )
                resposta.search?.map { item ->
                    Midia(
                        titulo = item.title,
                        tipo = "Filmes",
                        status = "Planejando",
                        nota = 0,
                        comentario = "",
                        imageUrl = if (item.poster == "N/A") "" else item.poster,
                        description = "Filme de: ${item.year}"
                    )
                } ?: emptyList()
            } catch (e: Exception) {
                Log.e("OMDB_ERRO", "Erro na busca de filmes: ${e.message}")
                emptyList()
            }

            else -> emptyList()
        }
    }
}