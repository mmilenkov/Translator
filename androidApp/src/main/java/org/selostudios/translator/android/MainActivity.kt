package org.selostudios.translator.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import org.selostudios.translator.Greeting
import org.selostudios.translator.android.core.presentation.Routes
import org.selostudios.translator.android.translation.presentation.AndroidTranslationViewModel
import org.selostudios.translator.android.translation.presentation.TranslationScreen
import org.selostudios.translator.android.voiceToText.presentation.AndroidVoiceToTextViewModel
import org.selostudios.translator.android.voiceToText.presentation.VoiceToTextScreen
import org.selostudios.translator.translation.presentation.TranslationEvent
import org.selostudios.translator.voiceToText.presentation.VoiceToTextEvent

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TranslatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TranslatorRoot()
                }
            }
        }
    }
}

@Composable
fun TranslatorRoot() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.TRANSLATE
    ) {
        composable(route = Routes.TRANSLATE) { backstackEntry ->
            val viewModel = hiltViewModel<AndroidTranslationViewModel>()
            val state by viewModel.state.collectAsState()
            val voiceRecordingResult by backstackEntry.savedStateHandle
                .getStateFlow<String?>("voiceRecordingResult", null)
                .collectAsState()

            LaunchedEffect(voiceRecordingResult) {
                viewModel.onEvent(TranslationEvent.SubmitVoiceResult(voiceRecordingResult))
                backstackEntry.savedStateHandle["voiceRecordingResult"] = null
            }

            TranslationScreen(
                state = state,
                onEvent = { event ->
                    when (event) {
                        is TranslationEvent.RecordAudio -> {
                            navController.navigate(Routes.VOICE_TO_TEXT + "/${state.fromLanguage.language.code}")
                        }
                        else -> {
                            viewModel.onEvent(event)
                        }
                    }
                }
            )
        }

        composable(
            route = Routes.VOICE_TO_TEXT + "/{code}",
            arguments = listOf(
                navArgument("code") {
                    type = NavType.StringType
                    defaultValue = "en"
                }
            )
        ) { backstackEntry ->
            val languageCode = backstackEntry.arguments?.getString("code") ?: "en"
            val viewModel = hiltViewModel<AndroidVoiceToTextViewModel>()
            val state by viewModel.state.collectAsState()

            VoiceToTextScreen(
                state = state,
                languageCode = languageCode,
                onResult = { spokenText ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("voiceRecordingResult", spokenText)

                    navController.popBackStack()
                },
                onEvent = { event ->
                    when (event) {
                        is VoiceToTextEvent.Close -> {
                            navController.popBackStack()
                        }
                        else -> viewModel.onEvent(event)
                    }
                }
            )
        }
    }
}
