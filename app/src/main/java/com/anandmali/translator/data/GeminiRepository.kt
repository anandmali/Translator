package com.anandmali.translator.data

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import javax.inject.Inject

class GeminiRepository @Inject constructor(private val model: GenerativeModel) {

    companion object {
        private const val TAG = "GeminiRepository"
    }


    suspend fun translateUsingGemini(toLanguage: String, sentence: String): String? {
        val prompt =
            "You are a translator. Your job is to translate a given sentence into given language. Translate give sentence '$sentence' into '$toLanguage' language. Be concise and accurate, return only one result for given language"
        return try {
            val response = model.generateContent(prompt = prompt)
            response.text
        } catch (e: Exception) {
            Log.e(TAG, "Error translating: ${e.message}")
            null
        }
    }

    suspend fun getLanguageOf(sentence: String): String? {
        val prompt =
            "You are a language translator. Identify language of given sentence '$sentence'. Return with the name of the language."
        return try {
            val responseLanguage = model.generateContent(prompt = prompt)
            responseLanguage.text
        } catch (e: Exception) {
            Log.d(TAG, "Error getting language of $sentence ${e.message}")
            null
        }
    }

}