package com.anandmali.translator.di

import com.anandmali.translator.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object GeminiModule {

    @Provides
    fun provideGeminiModule(): GenerativeModel {
        return GenerativeModel(
            apiKey = BuildConfig.apiKey,
            modelName = "gemini-1.5-flash",
            generationConfig = generationConfig {
                temperature = 0.4f
                maxOutputTokens = 10000
            }
        )
    }
}