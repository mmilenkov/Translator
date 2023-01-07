package org.selostudios.translator.translation.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.selostudios.translator.core.presentation.UiLanguage
import org.selostudios.translator.translation.data.history.FakeHistoryDataSource
import org.selostudios.translator.translation.data.history.HistoryItem
import org.selostudios.translator.translation.data.networking.FakeTranslationClient
import org.selostudios.translator.translation.domain.usecases.TranslationUseCase
import kotlin.test.BeforeTest
import kotlin.test.Test

//This is meant to show a sample of testing a flow and not be a full test suite
class TranslationViewModelTest {
    private lateinit var viewModel: TranslationViewModel
    private lateinit var client: FakeTranslationClient
    private lateinit var datasource: FakeHistoryDataSource

    @BeforeTest
    fun setup() {
        client = FakeTranslationClient()
        datasource = FakeHistoryDataSource()
        val useCase = TranslationUseCase(client, datasource)
        viewModel = TranslationViewModel(
            useCase,
            datasource,
            CoroutineScope(Dispatchers.Main)
        )
    }

    @Test
    fun `state and history items are combined properly`() = runBlocking {
        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState).isEqualTo(TranslationState())

            val item = HistoryItem(
                id = 0,
                fromLanguage = "en",
                fromText = "text",
                toLanguage = "en",
                toText = "text"
            )
            datasource.insertHistory(item)

            val newState = awaitItem()
            val expectedItem = UiHistoryItem(
                id = item.id!!,
                fromLanguage = UiLanguage.fromCode(item.fromLanguage),
                fromText = item.fromText,
                toLanguage = UiLanguage.fromCode(item.toLanguage),
                toText = item.toText
            )
            assertThat(newState.history.first()).isEqualTo(expectedItem)
        }
    }

    @Test
    fun `translation success with proper state update`() = runBlocking {
        viewModel.state.test {
            awaitItem()

            viewModel.onEvent(TranslationEvent.ChangeTranslationText("test"))
            awaitItem()
            viewModel.onEvent(TranslationEvent.Translate)

            val loadingState = awaitItem()
            assertThat(loadingState.isTranslating).isTrue()

            val resultState = awaitItem()
            assertThat(resultState.isTranslating).isFalse()
            assertThat(resultState.toText).isEqualTo(client.translatedText)
        }
    }
}