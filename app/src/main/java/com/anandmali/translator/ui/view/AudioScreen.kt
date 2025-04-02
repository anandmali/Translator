package com.anandmali.translator.ui.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anandmali.translator.R
import com.anandmali.translator.model.Language
import com.anandmali.translator.model.supportedLanguages
import com.anandmali.translator.ui.theme.TranslatorTheme


@Composable
fun AudioScreen(
    audioViewModel: AudioViewModel = viewModel(),
    launchSpeechRecogniser: ((String) -> Unit) -> Unit
) {
    val placeholderResult = stringResource(R.string.results_placeholder)
    var result by rememberSaveable { mutableStateOf(placeholderResult) }
    val uiState by audioViewModel.uiState.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    var languageSelected by remember { mutableStateOf(supportedLanguages[29]) }
    var speechRecognised by remember { mutableStateOf("") }
    val speechRecognizedLanguage by audioViewModel.speechLanguage.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.translator_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        ExtendedFloatingActionButton(
            onClick = {
                launchSpeechRecogniser { result ->
                    speechRecognised = result
                    audioViewModel.getLanguageOfText(result)
                }
            },
            icon = { Icon(Icons.Filled.Mic, "Floating button to speak") },
            text = { Text(text = "Click to speak") },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )

        SpokenText(speechRecognised)

        SpokenLanguage(speechRecognizedLanguage)

        SelectLanguageToTranslate(
            expanded,
            languageSelected,
            stateChange = { state ->
                expanded = state
            },
            languageSelected = { language ->
                languageSelected = language
            }
        )

        TranslateButton {
            audioViewModel.translateText(
                target = languageSelected.displayName,
                data = speechRecognised
            )
        }

        val scrollState = rememberScrollState()

        if (uiState is UiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            var textColor = MaterialTheme.colorScheme.onSurface
            if (uiState is UiState.Error) {
                textColor = MaterialTheme.colorScheme.error
                result = (uiState as UiState.Error).errorMessage
            } else if (uiState is UiState.Success) {
                textColor = MaterialTheme.colorScheme.onSurface
                result = (uiState as UiState.Success).outputText
            }

            Box(
                modifier = Modifier
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.secondary,
                        RoundedCornerShape(4.dp)
                    )
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        RoundedCornerShape(4.dp)
                    )
                    .fillMaxWidth()
            ) {
                Text(
                    text = result,
                    textAlign = TextAlign.Start,
                    color = textColor,
                    minLines = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(4.dp)
                )
            }
        }
    }

}

@Composable
fun SpokenText(speechRecognised: String) {
    Column {
        Text(
            text = stringResource(R.string.label_audio),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.secondary,
                    RoundedCornerShape(4.dp)
                )
                .background(
                    MaterialTheme.colorScheme.secondaryContainer,
                    RoundedCornerShape(4.dp)
                )
                .fillMaxWidth()
        ) {
            Text(
                text = speechRecognised.ifEmpty { stringResource(R.string.label_audio) },
                style = MaterialTheme.typography.bodyMedium,
                minLines = 2,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(4.dp)
            )
        }
    }

}

@Composable
fun SpokenLanguage(speechRecognizedLanguage: String) {
    Column {
        Text(
            text = stringResource(R.string.label_language_identified),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.secondary,
                    RoundedCornerShape(4.dp)
                )
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.secondaryContainer,
                    RoundedCornerShape(4.dp)
                )

        ) {

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Image(
                    painter = rememberVectorPainter(image = Icons.Filled.Language),
                    contentDescription = "Language Icon"
                )

                Text(
                    text = speechRecognizedLanguage,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp)
                )
            }
        }
    }
}

@Composable
fun SelectLanguageToTranslate(
    currentState: Boolean,
    currentLanguage: Language,
    stateChange: (state: Boolean) -> Unit,
    languageSelected: (language: Language) -> Unit
) {
    Column {
        Text(
            text = "Choose language",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.secondary,
                    RoundedCornerShape(4.dp)
                )
                .background(
                    MaterialTheme.colorScheme.secondaryContainer,
                    RoundedCornerShape(4.dp)
                )
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        stateChange(true)
                    }
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Image(
                    painter = rememberVectorPainter(image = Icons.Filled.Language),
                    contentDescription = "Language Icon"
                )
                Text(
                    text = currentLanguage.displayName,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                )
                Image(
                    painter = rememberVectorPainter(image = Icons.Filled.ArrowDropDown),
                    contentDescription = "DropDown Icon",
                    modifier = Modifier.size(24.dp)
                )
            }
            DropdownMenu(
                expanded = currentState,
                onDismissRequest = {
                    stateChange(false)
                }
            ) {
                supportedLanguages.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.displayName) },
                        onClick = {
                            languageSelected(option)
                            stateChange(false)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TranslateButton(
    onClick: () -> Unit
) {
    ElevatedButton(
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        modifier = Modifier
            .padding(top = 16.dp, bottom = 8.dp)
    ) {
        Text(
            "Translate",
            color = MaterialTheme.colorScheme.onSurface,
        )
    }


}

@PreviewLightDark
@Composable
fun SpokenTextPreview() {
    TranslatorTheme {
        Surface {
            SpokenText("Some random speech text")
        }
    }
}

@PreviewLightDark
@Composable
fun SpokenLanguagePreview() {
    TranslatorTheme {
        Surface {
            SpokenLanguage("English")
        }
    }
}

@PreviewLightDark
@Composable
fun SelectLanguageToTranslatePreview() {
    TranslatorTheme {
        Surface {
            SelectLanguageToTranslate(
                false,
                Language("Icelandic", "is"),
                {},
                {}
            )
        }
    }
}

@PreviewLightDark
@Composable
fun TranslateButtonPreview() {
    TranslatorTheme {
        Surface {
            TranslateButton { }
        }
    }
}
