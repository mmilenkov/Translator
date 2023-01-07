package org.selostudios.translator.android.voiceToText.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import org.selostudios.translator.voiceToText.domain.VoiceToTextParser
import org.selostudios.translator.voiceToText.presentation.VoiceToTextEvent
import org.selostudios.translator.voiceToText.presentation.VoiceToTextViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidVoiceToTextViewModel@Inject constructor(
    private val parser: VoiceToTextParser
): ViewModel() {

    private val viewModel by lazy {
        VoiceToTextViewModel(
            parser,
            viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: VoiceToTextEvent) {
        viewModel.onEvent(event)
    }
}