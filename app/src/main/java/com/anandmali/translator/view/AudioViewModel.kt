package com.anandmali.translator.view

import android.util.Log
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

    companion object {
        private const val TAG = "MyViewModel"
    }

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState.Initial)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _speechLanguage: MutableStateFlow<String> = MutableStateFlow("")
    val speechLanguage: StateFlow<String> = _speechLanguage.asStateFlow()

    fun translateText(target: String, data: String) {
        Log.d(TAG, "translateText: target = $target and data = $data")
        _uiState.value = UiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = geminiRepository.translateUsingGemini(target, data)
            result?.let {
                _uiState.value = UiState.Success(it.toString() ?: "")
            } ?: kotlin.run {
                _uiState.value = UiState.Error("Error translating the text")
            }
        }
    }

    fun detectLanguageOfText(data: String) {
        Log.d(TAG, "detectLanguageOfText: detecting language into MyViewModel class")
        viewModelScope.launch {
            val language = geminiRepository.getLanguageOf(data)
            _speechLanguage.value = language.toString()
        }
    }
}