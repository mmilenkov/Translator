package org.selostudios.translator.core.domain.util

import kotlinx.coroutines.flow.StateFlow

// Section from https://github.com/icerockdev/moko-mvvm
expect open class CommonStateFlow<T>(flow: StateFlow<T>): StateFlow<T>

fun <T> StateFlow<T>.toCommonStateFlow() = CommonStateFlow(this)