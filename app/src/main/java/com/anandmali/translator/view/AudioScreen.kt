package com.anandmali.translator.view


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.anandmali.translator.R
import com.anandmali.translator.model.supportedLanguages


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
                    audioViewModel.detectLanguageOfText(result)
                }
            },
            icon = { Icon(Icons.Filled.Mic, "Floating button to speak") },
            text = { Text(text = "Click to speak") },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        )

        Text(
            text = stringResource(R.string.label_audio),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        Text(
            text = speechRecognised.ifEmpty { stringResource(R.string.label_audio) },
            style = MaterialTheme.typography.bodyMedium,
            minLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondaryContainer)

        )

        Text(
            text = stringResource(R.string.label_language_identified),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clip(CircleShape)

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

        Text(
            text = "Choose language",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        expanded = true
                    }
                    .fillMaxWidth()
            ) {
                Image(
                    painter = rememberVectorPainter(image = Icons.Filled.Language),
                    contentDescription = "Language Icon"
                )
                Text(
                    text = languageSelected.displayName,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                )
                Image(
                    painter = rememberVectorPainter(image = Icons.Filled.ArrowDropDown),
                    contentDescription = "DropDown Icon",
                    modifier = Modifier.size(24.dp)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                supportedLanguages.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.displayName) },
                        onClick = {
                            languageSelected = option
                            expanded = false
                        }
                    )
                }
            }
        }

        ElevatedButton(onClick = {
            audioViewModel.translateText(
                target = languageSelected.displayName,
                data = speechRecognised
            )
        }, modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)) {
            Text("Translate")
        }


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
            Text(
                text = result,
                textAlign = TextAlign.Start,
                color = textColor,
                minLines = 2,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            )
        }
    }

}


@Preview(showSystemUi = true)
@Composable
fun AudioScreenPreview() {
    AudioScreen { }
}