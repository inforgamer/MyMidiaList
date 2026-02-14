package com.example.mymidialist.utils // <--- O PACOTE TEM QUE SER ESSE

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.tasks.await

object Tradutor {

    private val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.PORTUGUESE)
        .build()

    private val englishToPortugueseTranslator = Translation.getClient(options)

    suspend fun traduzir(textoOriginal: String): String {
        return try {
            val conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()

            // Baixa o modelo se necessário
            englishToPortugueseTranslator.downloadModelIfNeeded(conditions).await()

            // Traduz
            englishToPortugueseTranslator.translate(textoOriginal).await()
        } catch (e: Exception) {
            e.printStackTrace()
            textoOriginal // Devolve em inglês se der erro
        }
    }
}