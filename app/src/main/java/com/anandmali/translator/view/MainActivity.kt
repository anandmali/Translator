package com.anandmali.translator.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.anandmali.translator.ui.theme.TranslatorTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TranslatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AudioScreen(launchSpeechRecogniser = ::startSpeechRecognition)
                }
            }
        }

        initActivityResultLauncher()
    }

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var recogniserResultHandler: ((String) -> Unit)? = null

    private fun initActivityResultLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK && result.data != null) {
                    val resultData =
                        result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    val spokenData = resultData?.get(0) ?: ""
                    recogniserResultHandler?.invoke(spokenData)
                }
            }
    }

    private fun startSpeechRecognition(
        resultHandler: (String) -> Unit
    ) {
        recogniserResultHandler = resultHandler
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now ...")
        }
        try {
            activityResultLauncher.launch(intent)
        } catch (a: ActivityNotFoundException) {
            Log.e("Error", "Failed recognise the audio")
        }
    }

}