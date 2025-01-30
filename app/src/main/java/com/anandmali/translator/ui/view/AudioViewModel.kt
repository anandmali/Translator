package com.anandmali.translator.ui.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anandmali.translator.data.GeminiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioViewModel @Inject constructor(private val geminiRepository: GeminiRepository) :
    ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _speechLanguage: MutableStateFlow<String> = MutableStateFlow("English (Default)")
    val speechLanguage: StateFlow<String> = _speechLanguage.asStateFlow()

    fun translateText(target: String, data: String) {
        _uiState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = geminiRepository.translateUsingGemini(target, data)
            result?.let {
                _uiState.value = UiState.Success(it.toString() ?: "")
            } ?: run {
                _uiState.value = UiState.Error("Error translating the text")
            }
        }
    }

    fun getLanguageOfText(data: String) {
        viewModelScope.launch {
            _speechLanguage.value = "Recognising the language ..."
            val language = geminiRepository.getLanguageOf(data)
            _speechLanguage.value = language.toString()
        }
    }
}