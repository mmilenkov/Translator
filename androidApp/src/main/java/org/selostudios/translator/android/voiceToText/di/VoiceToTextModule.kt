package org.selostudios.translator.android.voiceToText.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import org.selostudios.translator.android.voiceToText.data.AndroidVoiceToTextParser
import org.selostudios.translator.voiceToText.domain.VoiceToTextParser

@Module
@InstallIn(ViewModelComponent::class)
object VoiceToTextModule {

    @Provides
    @ViewModelScoped
    fun provideVoiceToTextParser(context: Application) :VoiceToTextParser {
        return AndroidVoiceToTextParser(context)
    }
}