@file:OptIn(ExperimentalComposeUiApi::class)

package org.selostudios.translator.android.translation.presentation

import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import org.selostudios.translator.android.R
import org.selostudios.translator.android.translation.presentation.componenets.*
import org.selostudios.translator.translation.domain.TranslationError
import org.selostudios.translator.translation.presentation.TranslationEvent
import org.selostudios.translator.translation.presentation.TranslationState
import java.util.*

//Not passing the viewModel in order to fully uncouple it
@Composable
fun TranslationScreen(
    state: TranslationState,
    onEvent: (TranslationEvent) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = state.error) {
        val message = when (state.error) {
            TranslationError.SERVICE_UNAVAILABLE -> context.getString(R.string.error_service_unavailable)
            TranslationError.CLIENT_ERROR -> context.getString(R.string.client_error)
            TranslationError.SERVER_ERROR -> context.getString(R.string.server_error)
            TranslationError.UNKNOWN -> context.getString(R.string.unknown_error)
            else -> null
        }

        message?. let {
            Toast.makeText(context, message, Toast.LENGTH_LONG)
                .show()
            onEvent(TranslationEvent.OnErrorSeen)
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(TranslationEvent.RecordAudio) },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(75.dp)
            ) {
                Icon(imageVector = ImageVector.vectorResource(
                    id = R.drawable.mic),
                    contentDescription = stringResource(id = R.string.record_audio)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LanguageDropdown(
                        language = state.fromLanguage,
                        isOpen = state.isChoosingFromLanguage,
                        onClick = {
                                  onEvent(TranslationEvent.OpenFromLanguageDropdown)
                                  },
                        onDismiss = {
                                    onEvent(TranslationEvent.StopChoosingLanguage)
                                    },
                        onSelectLanguage = { selectedLanguage ->
                            onEvent(TranslationEvent.ChooseFromLanguage(selectedLanguage))
                        }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    SwapLanguagesButton(onClick = {
                        onEvent(TranslationEvent.SwapLanguages)
                    })
                    Spacer(modifier = Modifier.weight(1f))
                    LanguageDropdown(
                        language = state.toLanguage,
                        isOpen = state.isChoosingToLanguage,
                        onClick = {
                            onEvent(TranslationEvent.OpenToLanguageDropdown)
                        },
                        onDismiss = {
                            onEvent(TranslationEvent.StopChoosingLanguage)
                        },
                        onSelectLanguage = { selectedLanguage ->
                            onEvent(TranslationEvent.ChooseToLanguage(selectedLanguage))
                        }
                    )
                }
            }

            item {
                val  clipboardManager = LocalClipboardManager.current
                val keyboardController = LocalSoftwareKeyboardController.current
                val tts = rememberTextToSpeech()
                TranslateTextField(
                    fromLanguage = state.fromLanguage,
                    fromText = state.fromText,
                    toLanguage = state.toLanguage,
                    toText = state.toText,
                    isTranslating = state.isTranslating,
                    onTranslateClick = {
                        keyboardController?.hide()
                        onEvent(TranslationEvent.Translate)
                                       },
                    onTextChange = { text ->
                        onEvent(TranslationEvent.ChangeTranslationText(text))
                                   },
                    onCopyClick = { text ->
                        clipboardManager.setText(
                            buildAnnotatedString {
                                append(text)
                            }
                        )
                        Toast.makeText(
                            context,
                            context.getString(R.string.copied_to_clipboard),
                            Toast.LENGTH_LONG
                        ).show()
                                  },
                    onCloseClick = {
                                   onEvent(TranslationEvent.CloseTranslation)
                                   },
                    onSpeakerClick = {
                        tts.language = state.toLanguage.toLocale() ?: Locale.ENGLISH
                        tts.speak(
                            state.toText,
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            null
                        )
                                     },
                    onTextFieldClick = {
                        onEvent(TranslationEvent.EditTranslation)
                                       },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                if (state.history.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.history),
                        style = MaterialTheme.typography.h2
                    )
                }
            }

            items(state.history) { item ->
                TranslationHistoryItem(
                    item = item,
                    onClick = { onEvent(TranslationEvent.SelectHistoryItem(item)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}