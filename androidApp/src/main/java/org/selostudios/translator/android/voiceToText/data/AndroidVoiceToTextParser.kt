package org.selostudios.translator.android.voiceToText.data

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.SpeechRecognizer.ERROR_CLIENT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.selostudios.translator.android.R
import org.selostudios.translator.core.domain.util.CommonStateFlow
import org.selostudios.translator.core.domain.util.toCommonStateFlow
import org.selostudios.translator.voiceToText.domain.VoiceToTextParser
import org.selostudios.translator.voiceToText.domain.VoiceToTextParserState

class AndroidVoiceToTextParser(
    private val context: Application
): VoiceToTextParser, RecognitionListener {
    private val recognizer = SpeechRecognizer.createSpeechRecognizer(context)

    private val _state = MutableStateFlow(VoiceToTextParserState())
    override val state: CommonStateFlow<VoiceToTextParserState>
        get() = _state.toCommonStateFlow()

    override fun startListening(languageCode: String) {
        _state.update {
            VoiceToTextParserState()
        }

        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _state.update { state ->
                state.copy(
                    error = context.getString(R.string.error_speech_recognition_unavailable)
                )
            }
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
        }
        recognizer.setRecognitionListener(this)
        recognizer.startListening(intent)

        _state.update { state ->
            state.copy(
                isSpeaking = true
            )
        }
    }

    override fun stopListening() {
        _state.update {
            VoiceToTextParserState()
        }
        recognizer.stopListening()
    }

    override fun cancel() {
        recognizer.cancel()
    }

    override fun reset() {
        _state.value = VoiceToTextParserState()
    }

    //RecognitionListener
    override fun onReadyForSpeech(p0: Bundle?) {
        _state.update { state ->
            state.copy(
                error = null
            )
        }
    }
    override fun onRmsChanged(rmsDb: Float) {
        _state.update { state ->
            state.copy(
                powerRatio = rmsDb * (1f/ (12f - (-2f)))
            )
        }
    }

    override fun onEndOfSpeech() {
        _state.update { state ->
            state.copy(
                isSpeaking = false
            )
        }
    }

    override fun onError(p0: Int) {
        if (p0 == ERROR_CLIENT) {
            //This happens if you click the stop button before the translation finishes on
            // As such we are relying on the user to stop talking as the actual stop
            return
        }

        _state.update { state ->
            state.copy(
                error = "Error: $p0"
            )
        }
    }

    override fun onResults(result: Bundle?) {
        result?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.getOrNull(0)
            ?.let { recognisedText ->
                _state.update { state ->
                    state.copy(
                        result = recognisedText
                    )
                }
        }
    }

    override fun onPartialResults(p0: Bundle?) = Unit

    override fun onBeginningOfSpeech() = Unit

    override fun onEvent(p0: Int, p1: Bundle?) = Unit

    override fun onBufferReceived(p0: ByteArray?) = Unit

}