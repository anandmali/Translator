package com.anandmali.translator.data

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import javax.inject.Inject

class GeminiRepository @Inject constructor(private val model: GenerativeModel) {

    companion object {
        private const val TAG = "GeminiRepository"
    }


    suspend fun translateUsingGemini(toLanguage: String, sentence: String): String? {
        //Basic
//        val prompt =
//            "You are a translator. Your job is to translate a given sentence into given language. Translate give sentence '$sentence' into '$toLanguage' language. Be concise and accurate, return only one result for given language"

        //Making it intelligent
//        val prompt = "You are an intelligent language translator. " +
//                "Your job is to translate a given sentence, find and label the topic of the given " +
//                "sentence, and also share if there are any follow up questions should be asked. " +
//                "Translate give sentence '$sentence' into '$toLanguage' language. " +
//                "Be concise and accurate"

        //Training to be intelligent and concise
        val prompt = """
You are an intelligent language translator and information extractor. Your task is to process the given sentence and provide the following information in a structured text format:

1. Translation: The translation of the sentence into the specified language.
2. Topic: A concise label (1-3 words) representing the main topic of the sentence.
3. Follow_up_questions:  2-5 relevant follow-up questions that could be asked to explore the topic further.  If no follow-up questions are applicable, return an empty string.
4. Notes: 2-3 short, concise notes summarizing key information or insights from the sentence. If the sentence is too short to take notes return an empty string.

Sentence: '$sentence'
Target Language: '$toLanguage'

Output the entire response as a valid text.
"""
        return try {
            val response = model.generateContent(prompt = prompt)
            response.text
        } catch (e: Exception) {
            Log.e(TAG, "Error translating: ${e.message}")
            null
        }
    }

    suspend fun getLanguageOf(sentence: String): String? {
        val prompt = """
You are a language translator. Identify language of given sentence '$sentence'.

Sentence: '$sentence'

Return only one name of the language. If you do not know the language of the sentence, return 'Unknown' word as language.
"""
        return try {
            val responseLanguage = model.generateContent(prompt = prompt)
            responseLanguage.text
        } catch (e: Exception) {
            Log.d(TAG, "Error getting language of $sentence ${e.message}")
            null
        }
    }

}